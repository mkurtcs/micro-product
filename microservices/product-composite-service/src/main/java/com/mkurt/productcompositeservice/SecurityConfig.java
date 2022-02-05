package com.mkurt.productcompositeservice;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.http.HttpMethod.*;

@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .pathMatchers("/openapi/**").permitAll()
                .pathMatchers("/webjars/**").permitAll()
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers(POST, "/product-composite/**").hasAuthority("SCOPE_product:write")
                .pathMatchers(DELETE, "/product-composite/**").hasAuthority("SCOPE_product:write")
                .pathMatchers(GET, "/product-composite/**").hasAuthority("SCOPE_product:read")
                .anyExchange().authenticated() // URL'lere erişim izni verilmeden önce kullanıcının kimliğinin doğrulanmasını sağlar.
                .and()
                .oauth2ResourceServer().jwt(); // specifies that authorization will be based on OAuth 2.0 access tokens encoded as JWTs.
        return http.build();
    }
}

// By convention, OAuth 2.0 scopes need to be prefixed with SCOPE_ when checked for authority using Spring Security.

/** both the edge server and the product-composite service can act as OAuth 2.0 resource servers,
 * and the Swagger UI component can act as an OAuth client. */