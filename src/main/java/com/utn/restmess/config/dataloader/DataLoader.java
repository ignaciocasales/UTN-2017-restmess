package com.utn.restmess.config.dataloader;

import com.google.common.collect.Lists;
import com.utn.restmess.entities.User;
import com.utn.restmess.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    private List<User> userList = new ArrayList<>();

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);

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
        this.userList.add(new User(
                "Homer",
                "Simpson",
                "742 Evergreen Terrace",
                "1-(619)174-5105",
                "Springfield",
                "California",
                "United States",
                "HSimpson",
                encoder.encode("HSimpson123"),
                "HSimpson@mail.com"
        ));
        this.userList.add(new User(
                "Bart",
                "Simpson",
                "742 Evergreen Terrace",
                "1-(619)174-5106",
                "Springfield",
                "California",
                "United States",
                "BSimpson",
                encoder.encode("BSimpson123"),
                "BSimpson@mail.com"
        ));
        this.userList.add(new User(
                "Lisa",
                "Simpson",
                "742 Evergreen Terrace",
                "1-(619)174-5107",
                "Springfield",
                "California",
                "United States",
                "LSimpson",
                encoder.encode("LSimpson123"),
                "LSimpson@mail.com"
        ));
        this.userList.add(new User(
                "Rick",
                "Sanchez",
                "Earth (Dimension C-137)",
                "1-(619)174-5108",
                "Springfield",
                "California",
                "United States",
                "RSanchez",
                encoder.encode("RSanchez123"),
                "RSanchez@mail.com"
        ));
        this.userList.add(new User(
                "Morty",
                "Smith",
                "Earth (Dimension C-137)",
                "1-(619)174-5109",
                "Springfield",
                "California",
                "United States",
                "MSmith",
                encoder.encode("MSmith123"),
                "MSmith@mail.com"
        ));
        this.userList.add(new User(
                "Jerry",
                "Smith",
                "Earth (Dimension C-137)",
                "1-(619)174-5110",
                "Springfield",
                "California",
                "United States",
                "JSmith",
                encoder.encode("JSmith123"),
                "JSmith@mail.com"
        ));
    }
}
