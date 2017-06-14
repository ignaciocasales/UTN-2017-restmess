package com.utn.restmess.controllers;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.utn.restmess.Application;
import com.utn.restmess.config.util.SessionData;
import com.utn.restmess.entities.Message;
import com.utn.restmess.entities.User;
import com.utn.restmess.persistence.MessageRepository;
import com.utn.restmess.persistence.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by ignacio on 6/13/17.
 * <p>
 * UserControllerTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("default")
public class UserControllerTest {


    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private SessionData sessionData;

    private String sessionid;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);

    private User u;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.u = new User(
                "TesName",
                "TestSurname",
                "TestAddress",
                "TestPhone",
                "TestCity",
                "TestState",
                "TestCountry",
                "TestUsername",
                encoder.encode("TestPassword"),
                "TestEmail@testemail.com"
        );

        this.sessionid = this.sessionData.addSession(u);
    }

    @After
    public void setupAfter() throws Exception {
        this.userRepository.deleteAll();

        this.sessionData.removeSession(this.sessionid);
    }

    @Test
    public void showAllSuccess() throws Exception {
        this.u = userRepository.save(u);

        mockMvc.perform(
                get("/api/users")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void showAllNoUsersException() throws Exception {
        mockMvc.perform(
                get("/api/users")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
        )
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void showByNameSuccess() throws Exception {
        this.u = userRepository.save(u);

        mockMvc.perform(
                get("/api/users/search")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
                        .param("name", u.getFirstName())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void showByNameNoContent() throws Exception {
        mockMvc.perform(
                get("/api/users/search")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
                        .param("name", u.getFirstName())
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void showByNameEmptyParam() throws Exception {
        mockMvc.perform(
                get("/api/users/search")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
                        .param("name", "")
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void showByUsernameSuccess() throws Exception {
        this.u = userRepository.save(u);

        mockMvc.perform(
                get("/api/users/TestUsername")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void showByUsernameNoContent() throws Exception {
        mockMvc.perform(
                get("/api/users/TestUsername")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void createSuccess() throws Exception {
        URL url = Resources.getResource("user.json");
        String json = Resources.toString(url, Charsets.UTF_8);

        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void createDuplicateUsernameException() throws Exception {
        URL url = Resources.getResource("user.json");
        String json = Resources.toString(url, Charsets.UTF_8);

        this.u = userRepository.save(u);

        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
        )
                .andExpect(status().isConflict());
    }

    @Test
    public void createDuplicateEmailException() throws Exception {
        URL url = Resources.getResource("user.json");
        String json = Resources.toString(url, Charsets.UTF_8);

        this.u.setUsername("Diferent");

        this.u = userRepository.save(u);

        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
        )
                .andExpect(status().isConflict());
    }

    @Test
    public void destroySuccess() throws Exception {
        this.u = userRepository.save(u);

        mockMvc.perform(
                delete("/api/users")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
        )
                .andExpect(status().isOk());
    }

    @Test
    public void destroySuccessWithMessages() throws Exception {
        Message m = new Message(
                "TestUsername",
                "SomeOtherUser",
                "Message",
                Timestamp.from(Instant.now()),
                "Content",
                false,
                false
        );
        this.u = userRepository.save(u);

        m.setUser(u);

        messageRepository.save(m);

        mockMvc.perform(
                delete("/api/users")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
        )
                .andExpect(status().isOk());
    }
}
