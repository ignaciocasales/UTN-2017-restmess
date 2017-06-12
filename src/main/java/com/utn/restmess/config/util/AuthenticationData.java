package com.utn.restmess.config.util;

import org.joda.time.DateTime;

/**
 * Created by pablo on 01/11/16.
 * <p>
 * Authentication Data.
 */
public class AuthenticationData {

    private String username;
    private DateTime lastAction;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public DateTime getLastAction() {
        return lastAction;
    }

    public void setLastAction(DateTime lastAction) {
        this.lastAction = lastAction;
    }
}
