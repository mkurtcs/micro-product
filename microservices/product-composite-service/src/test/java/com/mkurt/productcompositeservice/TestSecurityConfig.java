package com.mkurt.productcompositeservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@TestConfiguration
public class TestSecurityConfig {

    /** The use of OAuth has been disabled when running Spring-based integration tests.
     * To prevent the OAuth machinery from kicking in when we are running integration tests, we disable it as follows:

     A security configuration, TestSecurityConfig, is added to be used during tests. It permits access to all resources: */

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf()
                .disable()
                .authorizeExchange()
                .anyExchange()
                .permitAll();

        return http.build();
    }
}

