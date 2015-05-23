package com.rlogin.domain.gjj;

/**
 * Created by changxx on 15/5/22.
 */
public class GjjAsynDomain {

    private GjjUser gjjUser;

    private String cookie;

    public GjjAsynDomain() {
    }

    public GjjAsynDomain(GjjUser gjjUser, String cookie) {
        this.gjjUser = gjjUser;
        this.cookie = cookie;
    }

    public GjjUser getGjjUser() {
        return gjjUser;
    }

    public void setGjjUser(GjjUser gjjUser) {
        this.gjjUser = gjjUser;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
