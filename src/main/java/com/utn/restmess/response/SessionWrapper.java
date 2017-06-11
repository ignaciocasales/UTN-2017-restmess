package com.utn.restmess.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ignacio on 6/10/17.
 * <p>
 * SessionWrapper
 */
public class SessionWrapper {

    @JsonProperty
    private String sessionid;

    public SessionWrapper() {
    }

    public SessionWrapper(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }
}
