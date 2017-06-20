package com.utn.restmess.controllers;

import com.google.common.collect.Lists;
import com.utn.restmess.services.NoUsersException;
import com.utn.restmess.services.UserService;
import com.utn.restmess.converter.UserConverter;
import com.utn.restmess.entities.Message;
import com.utn.restmess.entities.User;
import com.utn.restmess.persistence.MessageRepository;
import com.utn.restmess.persistence.UserRepository;
import com.utn.restmess.request.UserRequest;
import com.utn.restmess.response.ErrorMessageWrapper;
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
            Iterable<User> userIterable = userRepository.findAll();

            List<User> userList = Lists.newArrayList(userIterable);

            if (userList.size() > 0) {
                return new ResponseEntity<>(this.convertList(userList), HttpStatus.OK);
            } else {
                throw new NoUsersException("No hay usuarios.");
            }
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
            if (!name.isEmpty()) {
                String formattedName = name.substring(0, 1).toUpperCase() + name.substring(1);

                List<User> userList = userRepository.findByFirstName(formattedName);

                if (userList.size() > 0) {
                    return new ResponseEntity<>(this.convertList(userList), HttpStatus.OK);
                } else {
                    throw new NoUsersException("No hay usuarios con ese nombre.");
                }
            } else {
                throw new NoUsersException("Ingrese un usuario.");
            }
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
            User u = userRepository.findByUsername(username);

            if (u == null) {
                throw new NoUsersException("No hay usuarios con ese username.");
            }

            return new ResponseEntity<>(userConverter.convert(u), HttpStatus.OK);
        } catch (NoUsersException e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
            value = "/users",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity create(@RequestBody UserRequest request) {
        try {
            if (userRepository.countByUsername(request.getUsername()) > 0) {
                throw new DuplicateUsernameException("Nombre de usuario ya usado.");
            } else if (userRepository.countByEmail(request.getEmail()) > 0) {
                throw new DuplicateEmailException("Email ya registrado.");
            } else {
                User u = userService.newUser(request);

                return new ResponseEntity<>(userConverter.convert(u), HttpStatus.CREATED);
            }
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
            User u = userRepository.findByUsername(username);

            for (Message m :
                    u.getMsgList()) {
                messageRepository.delete(m.getId());
            }

            userRepository.delete(u.getId());

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
