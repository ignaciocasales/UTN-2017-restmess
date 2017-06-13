package com.utn.restmess.controllers;

import com.utn.restmess.Application;
import com.utn.restmess.config.util.SessionData;
import com.utn.restmess.entities.User;
import com.utn.restmess.persistence.UserRepository;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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

import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by ignacio on 6/12/17.
 * <p>
 * SessionControllerTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("default")
public class SessionControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionData sessionData;

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
        this.u = userRepository.save(u);
    }

    @Test
    public void loginSuccess() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(asList(
                        new BasicNameValuePair("user", "TestUsername"),
                        new BasicNameValuePair("password", "TestPassword")
                )))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void loginFailUnauthorized() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(asList(
                        new BasicNameValuePair("user", "test"),
                        new BasicNameValuePair("password", "test")
                )))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void logoutSuccess() throws Exception {
        String sessionid = sessionData.addSession(this.u);
        mockMvc.perform(get("/logout")
                .header("sessionid", sessionid))
                .andExpect(status().isAccepted());
    }
}
