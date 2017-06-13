package com.utn.restmess.Services;

import com.utn.restmess.entities.User;
import com.utn.restmess.persistence.UserRepository;
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

    public User newUser(
            String firstName,
            String lastName,
            String address,
            String phone,
            String city,
            String state,
            String country,
            String username,
            String password,
            String email
    ) {
        User u = new User();

        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setAddress(address);
        u.setPhone(phone);
        u.setCity(city);
        u.setState(state);
        u.setCountry(country);
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(encoder.encode(password));

        return userRepository.save(u);
    }
}
