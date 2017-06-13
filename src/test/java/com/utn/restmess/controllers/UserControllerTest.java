package com.utn.restmess.controllers;

import com.utn.restmess.Application;
import com.utn.restmess.config.util.SessionData;
import com.utn.restmess.entities.User;
import com.utn.restmess.persistence.UserRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private SessionData sessionData;

    private String sessionid;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);

    private User u;

    @Before
    public void setup() throws Exception {
        this.userRepository.deleteAll();

        this.sessionData.removeSession(this.sessionid);

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
    }

    @Test
    public void showAllSuccess() throws Exception {
        this.u = userRepository.save(u);

        this.sessionid = this.sessionData.addSession(u);

        mockMvc.perform(get("/api/users")
                .header("sessionid", this.sessionid)
                .header("user", this.u.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void showAllNoContent() throws Exception {
        this.sessionid = this.sessionData.addSession(u);

        mockMvc.perform(get("/api/users")
                .header("sessionid", this.sessionid)
                .header("user", this.u.getUsername()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void showByNameSuccess() throws Exception {
        this.u = userRepository.save(u);

        this.sessionid = this.sessionData.addSession(u);

        mockMvc.perform(get("/api/users/search")
                .header("sessionid", this.sessionid)
                .header("user", this.u.getUsername())
                .param("name", u.getFirstName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void showByNameNoContent() throws Exception {
        this.sessionid = this.sessionData.addSession(u);

        mockMvc.perform(get("/api/users/search")
                .header("sessionid", this.sessionid)
                .header("user", this.u.getUsername())
                .param("name", u.getFirstName()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void showByNameEmptyParam() throws Exception {
        this.sessionid = this.sessionData.addSession(u);

        mockMvc.perform(get("/api/users/search")
                .header("sessionid", this.sessionid)
                .header("user", this.u.getUsername())
                .param("name", ""))
                .andExpect(status().isNoContent());
    }
}
