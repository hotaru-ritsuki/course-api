package com.example.courseapi.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Data
@ConfigurationProperties("security.jwt")
public class JwtProperties {
    private String signingKey;
    private Long accessTokenExpiration;
    private Long refreshTokenExpiration;

    public Long getTokenTimeout(boolean isAccessToken) {
        return isAccessToken ? accessTokenExpiration : refreshTokenExpiration;
    }
}
