package com.utn.restmess.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ignacio on 6/10/17.
 * <p>
 * BeanConfig class.
 */
@Configuration
public class BeanConfig {

    @Bean
    public AuthFilter getAuthFilter() {
        return new AuthFilter();
    }
}
