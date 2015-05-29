package com.rlogin.domain;

/**
 * 验证码
 * Created by changxx on 15/5/27.
 */
public enum Vericode {

    GJJ(1, "http://www.njgjj.com/vericode.jsp", "公积金登陆验证码"),

    IPCRS(2, "https://ipcrs.pbccrc.org.cn/imgrc.do", "个人信用信息服务平台登陆验证码");

    private int code;

    private String url;

    private String des;

    public static Vericode getByCode(int code) {
        for (Vericode vericode : Vericode.values()) {
            if (vericode.getCode() == code) {
                return vericode;
            }
        }
        return null;
    }


    Vericode(int code, String url, String des) {
        this.code = code;
        this.url = url;
        this.des = des;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
