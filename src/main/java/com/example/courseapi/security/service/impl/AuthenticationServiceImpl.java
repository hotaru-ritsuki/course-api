package com.example.courseapi.security.service.impl;

import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.exception.SystemException;
import com.example.courseapi.exception.code.ErrorCode;
import com.example.courseapi.security.dto.JWTRefreshDTO;
import com.example.courseapi.security.dto.LoginRequestDTO;
import com.example.courseapi.security.dto.JWTTokenDTO;
import com.example.courseapi.security.dto.SignUpRequestDTO;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.security.service.AuthenticationService;
import com.example.courseapi.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public void register(SignUpRequestDTO signUpRequestDTO) {
        log.info("Attempting to register user with email {}", signUpRequestDTO.getEmail());
        try {
            // Check if user already exists
            if (userRepository.existsByEmail(signUpRequestDTO.getEmail())) {
                throw new SystemException("Incorrect credentials. Please check email or password.",
                        ErrorCode.BAD_REQUEST);
            }
            // Create new user
            User user = User.builder()
                    .firstName(signUpRequestDTO.getFirstName())
                    .lastName(signUpRequestDTO.getLastName())
                    .email(signUpRequestDTO.getEmail())
                    .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                    .role(Roles.STUDENT)
                    .build();
            userRepository.save(user);
            log.info("User registration successful for email {}", signUpRequestDTO.getEmail());
        } catch (Exception ex) {
            log.error("Error occurred during user registration for email {}: {}", signUpRequestDTO.getEmail(), ex.getMessage());
            throw ex;
        }
    }

    @Override
    public JWTTokenDTO login(LoginRequestDTO loginRequestDTO, HttpServletRequest request) {
        log.info("Attempting to login user with email {}", loginRequestDTO.getEmail());
        try {
            // Create authentication token and authenticate user
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequestDTO.getEmail(),
                    loginRequestDTO.getPassword()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            authenticationManager.authenticate(authToken);

            // Get authenticated user and generate access and refresh tokens
            User user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(() ->
                    new SystemException("User with email: " + loginRequestDTO.getEmail() + " not found.", ErrorCode.BAD_REQUEST));
            String jwtAccessToken = jwtService.generateJwtToken(user, true);
            String jwtRefreshToken = jwtService.generateJwtToken(user, false);
            log.info("User authentication successful for email {}", loginRequestDTO.getEmail());
            return JWTTokenDTO.builder()
                    .accessToken(jwtAccessToken)
                    .refreshToken(jwtRefreshToken)
                    .build();
        } catch (Exception ex) {
            log.error("Error occurred during user authentication for email {}: {}", loginRequestDTO.getEmail(), ex.getMessage());
            throw ex;
        }
    }

    @Override
    public JWTTokenDTO refresh(final JWTRefreshDTO jwtRefreshDTO, HttpServletRequest request) {
        log.info("Attempting to refresh JWT token for user with refresh token: {}", jwtRefreshDTO.getRefreshToken());
        final String refreshToken = jwtRefreshDTO.getRefreshToken();
        final String userEmail = jwtService.extractUsername(refreshToken);
        if (StringUtils.isBlank(userEmail)) {
            throw new SystemException("Failed to refresh JWT token. User email not found in refresh token.",
                    ErrorCode.UNAUTHORIZED);
        }
        User userDetails = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new SystemException("Failed to refresh JWT token. User not found for email:" + userEmail, ErrorCode.UNAUTHORIZED));
        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            if (jwtService.isJwtTokenValid(refreshToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("JWT token successfully refreshed for user: {}", userEmail);
            } else {
                log.error("Failed to refresh JWT token. Refresh token is not valid for user: {}", userEmail);
            }
        }
        String jwtAccessToken = jwtService.generateJwtToken(userDetails, true);
        return JWTTokenDTO.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(refreshToken) // return same refresh token
                .build();
    }
}
