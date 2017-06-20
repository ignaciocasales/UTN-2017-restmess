package com.utn.restmess.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by ignacio on 6/20/17.
 * <p>
 * Encrypter class.
 */
@Service
public class Encrypter {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);

    public String encrypt(String raw) {
        return encoder.encode(raw);
    }

    public Boolean matches(String hashed, String raw) {
        return encoder.matches(hashed, raw);
    }
}
