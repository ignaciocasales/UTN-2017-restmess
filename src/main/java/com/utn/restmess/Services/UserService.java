package com.utn.restmess.Services;

import com.utn.restmess.entities.User;
import com.utn.restmess.persistence.UserRepository;
import com.utn.restmess.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by ignacio on 6/10/17.
 * <p>
 * UserService
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);

    public User login(String username, String password) {

        User u = userRepository.findByUsername(username);

        if (u == null) {
            return null;
        }

        if (encoder.matches(password, u.getPassword())) {
            return u;
        }

        return null;
    }

    public User newUser(UserRequest userRequest) {
        User u = new User();

        u.setFirstName(userRequest.getFirstName());
        u.setLastName(userRequest.getLastName());
        u.setAddress(userRequest.getAddress());
        u.setPhone(userRequest.getPhone());
        u.setCity(userRequest.getCity());
        u.setState(userRequest.getState());
        u.setCountry(userRequest.getCountry());
        u.setUsername(userRequest.getUsername());
        u.setEmail(userRequest.getPassword());
        u.setPassword(encoder.encode(userRequest.getEmail()));

        return userRepository.save(u);
    }
}
