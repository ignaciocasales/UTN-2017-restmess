package com.utn.restmess.controllers;

import com.utn.restmess.converter.UserConverter;
import com.utn.restmess.entities.User;
import com.utn.restmess.persistence.MessageRepository;
import com.utn.restmess.persistence.UserRepository;
import com.utn.restmess.request.UserRequest;
import com.utn.restmess.response.ErrorMessageWrapper;
import com.utn.restmess.response.UserWrapper;
import com.utn.restmess.services.DuplicateEmailException;
import com.utn.restmess.services.DuplicateUsernameException;
import com.utn.restmess.services.NoUsersException;
import com.utn.restmess.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 6/7/17.
 * <p>
 * UserController class.
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverter userConverter;

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity showAll() {
        try {
            List<User> userList = userService.getAll();

            return new ResponseEntity<>(this.convertList(userList), HttpStatus.OK);
        } catch (NoUsersException e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/users/search", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity showByName(@RequestParam("name") String name) {
        try {
            List<User> userList = userService.getByName(name);

            return new ResponseEntity<>(this.convertList(userList), HttpStatus.OK);
        } catch (NoUsersException e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
            value = "/api/users/{username}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity showByUsername(@PathVariable("username") String username) {
        try {
            User u = userService.getByUsername(username);

            return new ResponseEntity<>(userConverter.convert(u), HttpStatus.OK);
        } catch (NoUsersException e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
            value = "/users",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity create(@RequestBody UserRequest request) {
        try {
            User u = userService.create(request);

            return new ResponseEntity<>(userConverter.convert(u), HttpStatus.CREATED);
        } catch (DuplicateUsernameException | DuplicateEmailException e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
            value = "/api/users",
            method = RequestMethod.DELETE
    )
    public ResponseEntity destroy(@RequestHeader("user") String username) {
        try {
            userService.destroy(username);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<UserWrapper> convertList(List<User> user) {
        List<UserWrapper> userWrapperArrayList = new ArrayList<>();

        for (User c : user) {
            userWrapperArrayList.add(userConverter.convert(c));
        }
        return userWrapperArrayList;
    }
}
