package com.utn.restmess.controllers;

import com.google.common.collect.Lists;
import com.utn.restmess.converter.UserConverter;
import com.utn.restmess.entities.User;
import com.utn.restmess.persistence.UserRepository;
import com.utn.restmess.request.UserRequest;
import com.utn.restmess.response.UserWrapper;
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
@SuppressWarnings("unused")
@RestController
@RequestMapping(path = "/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<List<UserWrapper>> showAll() {
        Iterable<User> userIterable = userRepository.findAll();

        List<User> userList = Lists.newArrayList(userIterable);

        if (userList.size() > 0) {
            return new ResponseEntity<>(this.convertList(userList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/users/{name}", method = RequestMethod.GET)
    public void showByName() {
        //TODO
    }

    @RequestMapping(value = "/users/{name}", method = RequestMethod.GET)
    public void showByUsername() {
        //TODO
    }

    @RequestMapping(value = "/users",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity create(@RequestBody UserRequest request) {
        try {
            User u = new User();

            u.setFirstName(request.getFirstName());
            u.setLastName(request.getLastName());
            u.setAddress(request.getAddress());
            u.setPhone(request.getPhone());
            u.setCity(request.getCity());
            u.setState(request.getState());
            u.setCountry(request.getCountry());
            u.setUsername(request.getUsername());
            u.setEmail(request.getEmail());
            u.setPassword(request.getPassword());

            userRepository.save(u);

            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.DELETE)
    public ResponseEntity destroy(@PathVariable("username") String username) {
        try {
            User u = userRepository.findByUsername(username);

            userRepository.delete(u.getId());

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
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
