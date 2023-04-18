package com.example.courseapi.security.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String jwtToken);

    String generateJwtToken(UserDetails userDetails);

    String generateJwtToken(UserDetails userDetails, Map<String, Object> extraClaims);

    boolean isJwtTokenValid(String jwtToken, UserDetails userDetails);

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
}
