package com.utn.restmess.config.util;

import com.utn.restmess.entities.User;
import org.joda.time.DateTime;

/**
 * Created by pablo on 01/11/16.
 * <p>
 * Authentication Data.
 */
public class AuthenticationData {

    private User user;
    private DateTime lastAction;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    DateTime getLastAction() {
        return lastAction;
    }

    void setLastAction(DateTime lastAction) {
        this.lastAction = lastAction;
    }
}
