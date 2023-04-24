package com.example.courseapi.security.service.impl;

import com.example.courseapi.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SIGNING_KEY = "24432646294A404E635266556A586E5A7234753778214125442A472D4B615064";
    private static final Integer REFRESH_TOKEN_EXPIRE_TIMEOUT = 60 * 1000;
    private static final Integer ACCESS_TOKEN_EXPIRE_TIMEOUT = 24 * 60 * 1000;

    @Override
    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    @Override
    public boolean isJwtTokenValid(String jwtToken, UserDetails userDetails) {
        final String username = extractUsername(jwtToken);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);
    }

    private boolean isTokenExpired(String jwtToken) {
        Date expirationDate = extractExpirationDate(jwtToken);
        return Objects.nonNull(expirationDate) && expirationDate.before(new Date());
    }

    private Date extractExpirationDate(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    @Override
    public String generateJwtToken(UserDetails userDetails, boolean isAccessToken) {
        return generateJwtToken(userDetails, new HashMap<>(), isAccessToken);
    }

    @Override
    public String generateJwtToken(UserDetails userDetails, Map<String, Object> extraClaims, boolean isAccessToken) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + getTokenTimeout(isAccessToken)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Integer getTokenTimeout(boolean isAccessToken) {
        return isAccessToken ? ACCESS_TOKEN_EXPIRE_TIMEOUT : REFRESH_TOKEN_EXPIRE_TIMEOUT;
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SIGNING_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
