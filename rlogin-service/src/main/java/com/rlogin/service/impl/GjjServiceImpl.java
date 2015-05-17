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
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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
import com.rlogin.service.GjjService;

@Service
public class GjjServiceImpl implements GjjService {

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
		//  纪录公积金用户, 明细
		GjjUser gjjUser = this.recordUser(certinum, pass, poolSelect, gjjResult);

		this.recordDetailList(cookie, gjjUser);

		result.setCode(gjjUser != null ? Result.SUCCESS : Result.ERROR);
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
	private GjjUser recordUser(String certinum, String pass, PoolSelect poolSelect, GjjResult gjjResult) {
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

		return gjjUser;
	}

	/**
	 * 纪录公积金详情
	 */
	private void recordDetailList(String cookie, GjjUser gjjUser) {
		HttpClient httpClient = HttpClientSupport.getHttpClient();

		HttpGet post = new HttpGet("http://www.njgjj.com/init.summer?_PROCID=70000002");
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
		// 详情页查询使用
		String dataPool = this.getDataPool(responseBody);

		try {
			String detailListStr = this.getDetailList(cookie, dataPool, gjjUser);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getDetailList(String cookie, String dataPool, GjjUser gjjUser) throws ClientProtocolException, IOException {
		HttpClient httpClient = HttpClientSupport.getHttpClient();

		HttpPost post = new HttpPost("http://njgjj.com/dynamictable?uuid=" + System.currentTimeMillis());

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair(
				"_DATAPOOL_",
				"rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGphdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB+AAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEAfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAAGHcIAAAAIAAAABV0AAdfQUNDTlVNdAAQMzIwMTAwMDI3Mzg4NDg4MHQAA19SV3QAAXd0AAtfVU5JVEFDQ05VTXB0AAdfUEFHRUlEdAAFc3RlcDF0AANfSVNzcgAOamF2YS5sYW5nLkxvbmc7i+SQzI8j3wIAAUoABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhw///////nJE10AAxfVU5JVEFDQ05BTUV0ACfljY7kuLrmioDmnK/mnInpmZDlhazlj7jljZfkuqznoJTnqbbmiYB0AAZfTE9HSVB0ABEyMDE1MDUxNzE2NDkzNDM1NXQACF9BQ0NOQU1FdAAJ6Lev5a2m5LqudAAJaXNTYW1lUGVydAAFZmFsc2V0AAdfUFJPQ0lEdAAINzAwMDAwMDJ0AAtfU0VORE9QRVJJRHQAEjQxMDUyNzE5ODcwNjA2NTQzWHQAEF9ERVBVVFlJRENBUkROVU10ABI0MTA1MjcxOTg3MDYwNjU0M1h0AAlfU0VORFRJTUV0AAoyMDE1LTA1LTE3dAALX0JSQU5DSEtJTkR0AAEwdAAJX1NFTkREQVRFdAAKMjAxNS0wNS0xOHQAE0NVUlJFTlRfU1lTVEVNX0RBVEVxAH4AInQABV9UWVBFdAAEaW5pdHQAB19JU0NST1BxAH4AIHQACV9QT1JDTkFNRXQAGOS4quS6uuaYjue7huS/oeaBr+afpeivonQAB19VU0JLRVlwdAAIX1dJVEhLRVlxAH4AIHh0AAhAU3lzRGF0ZXQAB0BTeXNEYXl0AAlAU3lzTW9udGh0AAhAU3lzVGltZXQACEBTeXNXZWVrdAAIQFN5c1llYXI="));
		params.add(new BasicNameValuePair("_APPLY", "0"));
		params.add(new BasicNameValuePair("_CHANNEL", "1"));
		params.add(new BasicNameValuePair("_PROCID", "70000002"));
		params.add(new BasicNameValuePair("accname", gjjUser.getName()));
		params.add(new BasicNameValuePair("accnum", gjjUser.getAccId()));
		params.add(new BasicNameValuePair("begdate", "2000-01-01"));
		params.add(new BasicNameValuePair("dynamicTable_configSqlCheck", "0"));
		params.add(new BasicNameValuePair("dynamicTable_currentPage", "0"));
		params.add(new BasicNameValuePair("dynamicTable_id", "datalist2"));
		params.add(new BasicNameValuePair("dynamicTable_nextPage", "1"));
		params.add(new BasicNameValuePair("dynamicTable_page", "/ydpx/70000002/700002_01.ydpx"));
		params.add(new BasicNameValuePair("dynamicTable_pageSize", "1000"));
		params.add(new BasicNameValuePair("dynamicTable_paging", "true"));
		params.add(new BasicNameValuePair("enddate", new DateTime().toString("yyyy-MM-dd")));
		params.add(new BasicNameValuePair("errorFilter", "1=1"));

		post.setEntity(new UrlEncodedFormEntity(params));

		post.addHeader("Cookie", cookie);

		// 创建响应处理器处理服务器响应内容
		ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
		// 执行请求并获取结果
		String responseBody = httpClient.execute(post, responseHandler);
		System.out.println("----------------------------------------");
		System.out.println(responseBody);
		System.out.println("----------------------------------------");

		return responseBody;
	}

	/**
	 * 详情页查询使用
	 * 
	 * @param html
	 * @return
	 */
	private String getDataPool(String html) {
		Document document = Jsoup.parse(html);
		Elements elements = document.getElementsByAttributeValue("name", "_DATAPOOL_");
		return elements != null && elements.size() > 0 ? elements.get(0).text() : "";
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

	@Override
	public GjjUser getGjjUser(String certinum) {
		GjjUserExample example = new GjjUserExample();
		example.createCriteria().andAccIdEqualTo(certinum);
		List<GjjUser> gjjUsers = gjjUserMapper.selectByExample(example);
		return gjjUsers.size() > 0 ? gjjUsers.get(0) : null;
	}

	@Override
	public GjjAccDetail getGjjAccDetail(String certinum) {
		GjjAccDetailExample example = new GjjAccDetailExample();
		example.createCriteria().andUserAccIdEqualTo(certinum);
		List<GjjAccDetail> accDetails = gjjAccDetailMapper.selectByExample(example);
		return accDetails.size() > 0 ? accDetails.get(0) : null;
	}

}
