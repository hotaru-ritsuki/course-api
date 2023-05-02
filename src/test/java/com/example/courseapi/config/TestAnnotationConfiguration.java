package com.example.courseapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;

@Configuration
public class TestAnnotationConfiguration {

    @Bean
    public AuthenticationPrincipalArgumentResolver createAuthenticationHandler() {
        return new AuthenticationPrincipalArgumentResolver();
    }
}
