package com.utn.restmess.controllers;

import com.utn.restmess.Services.UserService;
import com.utn.restmess.config.util.SessionData;
import com.utn.restmess.entities.User;
import com.utn.restmess.response.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ignacio on 6/10/17.
 * <p>
 * SessionController class.
 */
@RestController
@RequestMapping(
        value = "/",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class SessionController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionData sessionData;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<SessionWrapper> getById(
            @RequestParam("user") String username,
            @RequestParam("password") String password
    ) {
        try {
            User user = userService.login(username, password);

            if (null != user) {
                String sessionid = sessionData.addSession(user);
                return new ResponseEntity<>(new SessionWrapper(sessionid), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping("/logout")
    public @ResponseBody
    ResponseEntity getById(@RequestHeader("sessionid") String sessionid) {
        try {
            sessionData.removeSession(sessionid);

            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
