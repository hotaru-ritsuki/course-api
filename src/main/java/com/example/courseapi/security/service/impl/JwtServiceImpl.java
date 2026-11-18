package com.example.courseapi.security.service.impl;

import com.example.courseapi.config.JwtProperties;
import com.example.courseapi.domain.RefreshToken;
import com.example.courseapi.domain.User;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.repository.RefreshTokenRepository;
import com.example.courseapi.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Override
    public String generateJwtAccessToken(final UserDetails userDetails) {
        return generateJwtToken(userDetails, new HashMap<>(), true);
    }

    @Override
    @Transactional
    public String generateJwtRefreshToken(User user) {
        refreshTokenRepository.deleteAllByUserId(user.getId());
        refreshTokenRepository.flush();

        LocalDateTime issuedAt  = LocalDateTime.now();
        LocalDateTime expirationDate = issuedAt.plus(jwtProperties.getRefreshTokenExpiration(), ChronoUnit.MILLIS);

        String jwtRefreshToken = Jwts.builder()
                .claims(new HashMap<>())
                .subject(user.getUsername())
                .issuedAt(Date.from(issuedAt.atZone(ZoneId.systemDefault()).toInstant()))
                .expiration(Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(jwtRefreshToken)
                .build();
        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

    @Override
    public String generateJwtToken(
            final UserDetails userDetails, final Map<String, Object> extraClaims, boolean isAccessToken) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getTokenTimeout(isAccessToken)))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public String extractUsername(final String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public boolean isJwtTokenValid(final String jwtToken, final UserDetails userDetails) {
        final String username = extractUsername(jwtToken);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);
    }

    @Override
    @Transactional
    public boolean isJwtRefreshTokenValid(final String jwtRefreshToken, final UserDetails userDetails) {
        return refreshTokenRepository.existsByTokenAndUserEmail(jwtRefreshToken, userDetails.getUsername())
                && isJwtTokenValid(jwtRefreshToken, userDetails);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSigningKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(final String jwtToken) {
        Date expirationDate = extractExpirationDate(jwtToken);
        return Objects.nonNull(expirationDate) && expirationDate.before(new Date());
    }

    private Claims extractAllClaims(final String jwtToken) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    private Date extractExpirationDate(final String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }
}
