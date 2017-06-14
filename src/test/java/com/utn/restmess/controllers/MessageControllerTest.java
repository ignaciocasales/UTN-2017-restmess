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
 * Created by ignacio on 6/14/17.
 * <p>
 * MessageControllerTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("default")
public class MessageControllerTest {

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

    private Message m;

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

        this.m = new Message(
                "SomeOtherUser",
                "TestUsername",
                "Message",
                Timestamp.from(Instant.now()),
                "Content",
                false,
                false
        );

        this.sessionid = this.sessionData.addSession(u);
    }

    @After
    public void setupAfter() throws Exception {
        this.userRepository.deleteAll();

        this.messageRepository.deleteAll();

        this.sessionData.removeSession(this.sessionid);
    }

    @Test
    public void inboxSuccess() throws Exception {
        this.u = userRepository.save(u);

        this.m.setUser(this.u);

        this.m = messageRepository.save(m);

        mockMvc.perform(
                get("/api/messages")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }


    @Test
    public void inboxNoMessagesException() throws Exception {
        this.u = userRepository.save(u);

        mockMvc.perform(
                get("/api/messages")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void inboxNoUsersException() throws Exception {
        mockMvc.perform(
                get("/api/messages")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void sentSuccess() throws Exception {
        this.u = userRepository.save(u);

        m.setSender("TestUsername");
        m.setRecipients("SomeOtherUser");
        this.m.setUser(this.u);

        this.m = messageRepository.save(m);

        mockMvc.perform(
                get("/api/messages/sent")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }


    @Test
    public void sentNoMessagesException() throws Exception {
        this.u = userRepository.save(u);

        mockMvc.perform(
                get("/api/messages/sent")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void sentNoUsersException() throws Exception {
        mockMvc.perform(
                get("/api/messages/sent")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void starredSuccess() throws Exception {
        this.u = userRepository.save(u);

        m.setStarred(true);
        this.m.setUser(this.u);

        this.m = messageRepository.save(m);

        mockMvc.perform(
                get("/api/messages/starred")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())

        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }


    @Test
    public void starredNoMessagesException() throws Exception {
        this.u = userRepository.save(u);

        mockMvc.perform(
                get("/api/messages/starred")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void starredNoUsersException() throws Exception {
        mockMvc.perform(
                get("/api/messages/starred")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void trashedSuccess() throws Exception {
        this.u = userRepository.save(u);

        m.setDeleted(true);
        this.m.setUser(this.u);

        this.m = messageRepository.save(m);

        mockMvc.perform(
                get("/api/messages/trashed")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }


    @Test
    public void trashedNoMessagesException() throws Exception {
        this.u = userRepository.save(u);

        mockMvc.perform(
                get("/api/messages/trashed")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void trashedNoUsersException() throws Exception {
        mockMvc.perform(
                get("/api/messages/trashed")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void sendSuccess() throws Exception {
        URL url = Resources.getResource("message.json");
        String json = Resources.toString(url, Charsets.UTF_8);

        User recipient = new User(
                "TesRecipient",
                "TestSurname",
                "TestAddress",
                "TestPhone",
                "TestCity",
                "TestState",
                "TestCountry",
                "recipient",
                encoder.encode("TestPassword"),
                "recipient@testemail.com"
        );

        this.u = userRepository.save(u);

        userRepository.save(recipient);

        mockMvc.perform(
                post("/api/messages")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
        )
                .andExpect(status().isCreated());
    }

    @Test
    public void sendNoUsersException() throws Exception {
        URL url = Resources.getResource("message.json");
        String json = Resources.toString(url, Charsets.UTF_8);

        mockMvc.perform(
                post("/api/messages")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void patchStarSuccess() throws Exception {
        URL url = Resources.getResource("patchmessagestar.json");
        String json = Resources.toString(url, Charsets.UTF_8);

        this.u = userRepository.save(u);

        this.m.setUser(this.u);

        this.m = messageRepository.save(m);

        mockMvc.perform(
                patch("/api/messages/" + m.getId())
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
        )
                .andExpect(status().isAccepted());
    }

    @Test
    public void patchDeleteSuccess() throws Exception {
        URL url = Resources.getResource("patchmessagedelete.json");
        String json = Resources.toString(url, Charsets.UTF_8);

        this.u = userRepository.save(u);

        this.m.setUser(this.u);

        this.m = messageRepository.save(m);

        mockMvc.perform(
                patch("/api/messages/" + m.getId())
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
        )
                .andExpect(status().isAccepted());
    }

    @Test
    public void patchNoMessage() throws Exception {
        URL url = Resources.getResource("patchmessagestar.json");
        String json = Resources.toString(url, Charsets.UTF_8);

        this.u = userRepository.save(u);

        mockMvc.perform(
                patch("/api/messages/1")
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void patchForbidden() throws Exception {
        URL url = Resources.getResource("patchmessagestar.json");
        String json = Resources.toString(url, Charsets.UTF_8);

        this.u = userRepository.save(u);

        User otherUser = userRepository.save(new User(
                "OtherUser",
                "TestSurname",
                "TestAddress",
                "TestPhone",
                "TestCity",
                "TestState",
                "TestCountry",
                "OtherUser",
                encoder.encode("TestPassword"),
                "OtherUser@testemail.com"
        ));

        this.m.setUser(otherUser);

        this.m = messageRepository.save(m);

        mockMvc.perform(
                patch("/api/messages/" + m.getId())
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    public void patchNotImplemented() throws Exception {
        URL url = Resources.getResource("patchnotimplementedmessage.json");
        String json = Resources.toString(url, Charsets.UTF_8);

        this.u = userRepository.save(u);

        this.m.setUser(this.u);

        this.m = messageRepository.save(m);

        mockMvc.perform(
                patch("/api/messages/" + m.getId())
                        .header("sessionid", this.sessionid)
                        .header("user", this.u.getUsername())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json)
        )
                .andExpect(status().isNotImplemented());
    }
}
