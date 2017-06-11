package com.utn.restmess.config.util;

import com.utn.restmess.entities.User;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * Created by pablo on 01/11/16.
 * <p>
 * SessionData.
 */
@SuppressWarnings("unused")
@Service
public class SessionData {

    final static Logger logger = Logger.getLogger(SessionData.class);
    private HashMap<String, AuthenticationData> sessionData;

    @Value("${session.expiration}")
    private int expirationTime;


    public SessionData() {
        this.sessionData = new HashMap<>();
    }

    public String addSession(User usuario) {
        String sessionId = UUID.randomUUID().toString();
        AuthenticationData authData = new AuthenticationData();
        authData.setUser(usuario);
        authData.setLastAction(new DateTime());
        this.sessionData.put(sessionId, authData);
        return sessionId;
    }


    public void removeSession(String sessionId) {
        sessionData.remove(sessionId);
    }

    public AuthenticationData getSession(String sessionId) {
        AuthenticationData authData = this.sessionData.get(sessionId);
        if (authData != null) {
            return authData;
        } else {
            return null;
        }
    }

    @Scheduled(fixedRate = 5000)
    public void checkSessions() {
        //System.out.println("Checking sessions");
        Set<String> sessionsId = this.sessionData.keySet();
        for (String sessionId : sessionsId) {
            AuthenticationData aData = this.sessionData.get(sessionId);
            if (aData.getLastAction().plusSeconds(expirationTime).isBefore(System.currentTimeMillis())) {
                //System.out.println("Deleting sessionId = " + sessionId);
                this.sessionData.remove(sessionId);
            }
        }
    }

}
