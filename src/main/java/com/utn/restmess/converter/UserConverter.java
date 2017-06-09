package com.utn.restmess.converter;

import com.utn.restmess.entities.User;
import com.utn.restmess.response.UserWrapper;
import org.springframework.stereotype.Component;

/**
 * Created by ignacio on 6/7/17.
 * <p>
 * User Converter.
 */
@Component
public class UserConverter {

    public UserConverter() {
    }

    public UserWrapper convert(User user) {
        UserWrapper u = new UserWrapper();

        u.setId(user.getId());
        u.setFirstName(user.getFirstName());
        u.setLastName(user.getLastName());
        u.setAddress(user.getAddress());
        u.setPhone(user.getPhone());
        u.setCity(user.getCity());
        u.setState(user.getState());
        u.setCountry(user.getCountry());
        u.setUsername(user.getUsername());
        u.setEmail(user.getEmail());

        return u;
    }
}
