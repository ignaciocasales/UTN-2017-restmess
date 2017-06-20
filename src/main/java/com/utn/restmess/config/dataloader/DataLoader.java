package com.utn.restmess.config.dataloader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.utn.restmess.entities.User;
import com.utn.restmess.persistence.UserRepository;
import com.utn.restmess.services.Encrypter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ignacio on 6/10/17.
 * <p>
 * Custom data loader.
 */
@Component
@Profile("dev")
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Encrypter encrypter;

    private List<User> userList = new ArrayList<>();

    private final static Logger logger = Logger.getLogger(DataLoader.class);

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (Lists.newArrayList(userRepository.findAll()).isEmpty()) {
            this.generateUsers();

            for (User u :
                    userList) {
                userRepository.save(u);
            }
        }
    }

    private void generateUsers() {
        URL url = Resources.getResource("users.json");
        ObjectMapper mapper = new ObjectMapper();

        try {
            User[] usersArray = mapper.readValue(url, User[].class);

            this.userList = new ArrayList<>(Arrays.asList(usersArray));
        } catch (IOException e) {
            logger.error("Can't read users.json");
        }
    }
}
