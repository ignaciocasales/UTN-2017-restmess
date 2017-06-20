package com.utn.restmess.config.util;

import com.utn.restmess.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import static org.joda.time.DateTime.now;

/**
 * Created by pablo on 01/11/16.
 * <p>
 * SessionData class.
 */
@Service
public class SessionData {

    private HashMap<String, AuthenticationData> authenticationData;

    @Value("${session.expiration}")
    private int expirationTime;


    public SessionData() {
        this.authenticationData = new HashMap<>();
    }

    public String addSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        AuthenticationData authData = new AuthenticationData();
        authData.setUsername(user.getUsername());
        authData.setLastAction(now());
        this.authenticationData.put(sessionId, authData);
        return sessionId;
    }


    public void removeSession(String sessionId) {
        authenticationData.remove(sessionId);
    }

    public AuthenticationData getSession(String sessionId) {
        AuthenticationData authData = this.authenticationData.get(sessionId);
        if (authData != null) {
            authData.setLastAction(now());
            return authData;
        } else {
            return null;
        }
    }

    @Scheduled(fixedRate = 5000)
    public void checkSessions() {
        Set<String> sessionsId = this.authenticationData.keySet();
        for (String sessionId : sessionsId) {
            AuthenticationData aData = this.authenticationData.get(sessionId);
            if (aData.getLastAction().plusSeconds(expirationTime).isBefore(System.currentTimeMillis())) {
                this.authenticationData.remove(sessionId);
            }
        }
    }
}
