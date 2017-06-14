package com.utn.restmess.config.dataloader;

import com.utn.restmess.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by ignacio on 6/14/17.
 * <p>
 * DataloaderTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("dev")
public class DataloaderTest {

    @Autowired
    private DataLoader dataLoader;

    @Test
    public void contextLoads() throws Exception {
        assertThat(dataLoader).isNotNull();
    }
}
