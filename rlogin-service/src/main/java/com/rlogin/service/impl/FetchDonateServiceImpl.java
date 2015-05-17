package com.rlogin.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rlogin.common.frame.Result;
import com.rlogin.common.http.HttpClientSupport;
import com.rlogin.common.http.NJReserveResponseHandler;
import com.rlogin.common.util.DateUtils;
import com.rlogin.common.util.JSONUtils;
import com.rlogin.dao.mapper.gjj.GjjAccDetailMapper;
import com.rlogin.dao.mapper.gjj.GjjUserMapper;
import com.rlogin.domain.gjj.GjjAccDetail;
import com.rlogin.domain.gjj.GjjAccDetailExample;
import com.rlogin.domain.gjj.GjjResult;
import com.rlogin.domain.gjj.GjjUser;
import com.rlogin.domain.gjj.GjjUserExample;
import com.rlogin.domain.gjj.PoolSelect;

@Service
public class FetchDonateServiceImpl implements FetchDonateService {

	@Autowired
	private GjjUserMapper gjjUserMapper;

	@Autowired
	private GjjAccDetailMapper gjjAccDetailMapper;

	@Override
	public Result fetchService(String certinum, String pass, String cookie) {
		Result result = new Result();
		// 拿到数据总线
		PoolSelect poolSelect = this.getPoolSelectByHtml(cookie);

		HttpClient httpClient = HttpClientSupport.getHttpClient();
		HttpPost post = new HttpPost("http://www.njgjj.com/command.summer?uuid=" + System.currentTimeMillis());

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("accname", poolSelect.get_ACCNAME()));
		params.add(new BasicNameValuePair("accnum", poolSelect.get_ACCNUM()));
		params.add(new BasicNameValuePair("certinum", certinum));
		params.add(new BasicNameValuePair("prodcode", "1"));
		params.add(new BasicNameValuePair("_PROCID", "80000003"));
		params.add(new BasicNameValuePair("_PAGEID", "step1"));

		try {
			post.setEntity(new UrlEncodedFormEntity(params));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");
		post.addHeader("Cookie", cookie);

		// 创建响应处理器处理服务器响应内容
		ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
		// 执行请求并获取结果
		String responseBody = null;
		try {
			responseBody = httpClient.execute(post, responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GjjResult gjjResult = JSONUtils.jsonToObject(responseBody, GjjResult.class);
		//  纪录公积金用户
		Integer num = this.recordUser(certinum, pass, poolSelect, gjjResult);
		result.setCode(num > 0 ? Result.SUCCESS : Result.ERROR);
		return result;
	}

	/**
	 * 获取页面中的数据总线
	 * 
	 * @return
	 */
	private PoolSelect getPoolSelectByHtml(String cookie) {
		HttpClient httpClient = HttpClientSupport.getHttpClient();

		HttpGet post = new HttpGet("http://www.njgjj.com/init.summer?_PROCID=80000032");

		post.addHeader("Cookie", cookie);

		// 创建响应处理器处理服务器响应内容
		ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
		// 执行请求并获取结果
		String responseBody = null;
		try {
			responseBody = httpClient.execute(post, responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.getPoolSelect(responseBody);
	}

	/**
	 * 获取页面中的数据总线
	 * 
	 * @return
	 */
	private PoolSelect getPoolSelect(String responseBody) {
		String str = "var poolSelect = ";
		String poolSelectAfter = responseBody.substring(responseBody.indexOf(str) + str.length());
		String poolSelectStr = poolSelectAfter.substring(0, poolSelectAfter.indexOf("}") + 1);

		String accnum = this.getPoolSelectItem("_ACCNUM", poolSelectStr);
		String accname = this.getPoolSelectItem("_ACCNAME", poolSelectStr);
		String certinum = this.getPoolSelectItem("_DEPUTYIDCARDNUM", poolSelectStr);
		String unitaccname = this.getPoolSelectItem("_UNITACCNAME", poolSelectStr);
		PoolSelect poolSelect = new PoolSelect(accnum, unitaccname, accname, certinum);
		return poolSelect;
	}

	private String getPoolSelectItem(String key, String poolSelect) {
		String mark = "'" + key + "': '";
		String val = poolSelect.substring(poolSelect.indexOf(mark) + mark.length());
		val = val.substring(0, val.indexOf("'"));
		return val;
	}

	/**
	 * 登陆后纪录用户
	 * 
	 * @param certinum
	 * @param pass
	 * @param poolSelect
	 * @return
	 */
	private Integer recordUser(String certinum, String pass, PoolSelect poolSelect, GjjResult gjjResult) {
		// 公积金用户表
		GjjUser gjjUser = new GjjUser();
		gjjUser.setAccId(certinum);
		gjjUser.setAccPwd(pass);
		gjjUser.setName(poolSelect.get_ACCNAME());
		gjjUser.setIdCard(certinum);
		gjjUser.setGjjAcc(poolSelect.get_ACCNUM());
		gjjUser.setLoginTime(new Date());

		GjjUserExample userExample = new GjjUserExample();
		userExample.createCriteria().andAccIdEqualTo(certinum);

		gjjUser.setBalance(this.getDouble(gjjResult.getData(), "amt1"));
		gjjUser.setmContribute(this.getDouble(gjjResult.getData(), "amt2"));
		gjjUser.setGjjRate(this.getDouble(gjjResult.getData(), "unitprop"));
		gjjUser.setAccStatus(this.getInteger(gjjResult.getData(), "unitaccstate"));
		gjjUser.setLoginTime(new Date());

		GjjUser dbGjjUser = this.getGjjUser(userExample);
		if (dbGjjUser == null) {
			// 没该身份证的账号，插入用户基本信息
			gjjUserMapper.insert(gjjUser);
		} else {
			gjjUser.setId(dbGjjUser.getId());
			gjjUserMapper.updateByPrimaryKey(gjjUser);
		}

		// 公积金详情表
		GjjAccDetail accDetail = new GjjAccDetail();
		accDetail.setUserAccId(certinum);
		accDetail.setProductCode(this.getInteger(gjjResult.getData(), "prodcode"));
		accDetail.setTeamAcc(gjjResult.getData().get("unitaccnum"));
		accDetail.setTeamName(gjjResult.getData().get("unitaccname"));
		accDetail.setAccCreateTime(DateUtils.strToDate(gjjResult.getData().get("opnaccdate")));// 开户日期
		accDetail.setAccStatus(this.getInteger(gjjResult.getData(), "indiaccstate"));// 账户状态
		accDetail.setUserMContribute(this.getDouble(gjjResult.getData(), "amt2"));// 月缴纳额
		accDetail.setBalance(gjjUser.getBalance());// 当前余额
		String lastTimeStr = gjjResult.getData().get("lpaym");
		lastTimeStr = lastTimeStr.substring(0, 4) + "-" + lastTimeStr.substring(4);
		accDetail.setLastTime(DateUtils.strToDate(lastTimeStr));//
		// 最后汇款月
		accDetail.setUserRate(this.getDouble(gjjResult.getData(), "indiprop"));// 缴存比例
		accDetail.setTeamRate(this.getDouble(gjjResult.getData(), "unitprop"));// 单位比例
		accDetail.setTeamCreateTime(DateUtils.strToDate(gjjResult.getData().get("begdate")));// 单位开户日期
		accDetail.setTeamStatus(this.getInteger(gjjResult.getData(), "unitaccstate"));// 单位账户状态
		accDetail.setBank(gjjResult.getData().get("instcode"));
		accDetail.setParseTime(new Date());// 数据解析时间

		GjjAccDetailExample accDetailExample = new GjjAccDetailExample();
		accDetailExample.createCriteria().andUserAccIdEqualTo(certinum);
		List<GjjAccDetail> dbGjjAccDetails = gjjAccDetailMapper.selectByExample(accDetailExample);
		if (dbGjjAccDetails != null && dbGjjAccDetails.size() > 0) {
			// 已经存在，更新
			accDetail.setId(dbGjjAccDetails.get(0).getId());
			gjjAccDetailMapper.updateByPrimaryKey(accDetail);
		} else {
			gjjAccDetailMapper.insert(accDetail);
		}

		return 1;
	}

	private GjjUser getGjjUser(GjjUserExample userExample) {
		List<GjjUser> gjjUsers = gjjUserMapper.selectByExample(userExample);
		if (gjjUsers.size() > 0) {
			return gjjUsers.get(0);
		}
		return null;
	}

	private Double getDouble(Map<String, String> map, String key) {
		try {
			return Double.parseDouble(map.get(key));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Integer getInteger(Map<String, String> map, String key) {
		try {
			return Integer.parseInt(map.get(key));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
