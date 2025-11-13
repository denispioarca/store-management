package com.example.store_management.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for development (needed for H2 console)
                .csrf(csrf -> csrf.disable())

                // Allow H2 console to be displayed in a frame
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                // Allow all requests for now (we will secure endpoints later with JWT)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}