package com.example.courseapi.security.service.impl;

import com.example.courseapi.domain.Student;
import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.security.dto.JWTRefreshDTO;
import com.example.courseapi.security.dto.LoginRequestDTO;
import com.example.courseapi.security.dto.JWTTokenDTO;
import com.example.courseapi.security.dto.SignUpRequestDTO;
import com.example.courseapi.exception.UserNotFoundException;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    public JWTTokenDTO register(SignUpRequestDTO signUpRequestDTO) {
        User user = User.builder()
                .firstName(signUpRequestDTO.getFirstName())
                .lastName(signUpRequestDTO.getFirstName())
                .email(signUpRequestDTO.getEmail())
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .role(Roles.STUDENT)
                .build();
        userRepository.save(user);
        String jwtAccessToken = jwtService.generateJwtToken(user, true);
        String jwtRefreshToken = jwtService.generateJwtToken(user, false);
        return JWTTokenDTO.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

    public JWTTokenDTO authenticate(LoginRequestDTO loginRequestDTO) {
        log.info("Attempt to login user with username : {}", loginRequestDTO.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(UserNotFoundException::new);
        String jwtAccessToken = jwtService.generateJwtToken(user, true);
        String jwtRefreshToken = jwtService.generateJwtToken(user, false);
        return JWTTokenDTO.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

    @Override
    public JWTTokenDTO refresh(final JWTRefreshDTO jwtRefreshDTO, HttpServletRequest request) {
        final String refreshToken = jwtRefreshDTO.getRefreshToken();
        if (StringUtils.isBlank(refreshToken)) {
            throw new RuntimeException();
        }
        final String userEmail = jwtService.extractUsername(refreshToken);
        if (StringUtils.isBlank(userEmail)) {
            throw new RuntimeException();
        }
        User userDetails = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            if (jwtService.isJwtTokenValid(refreshToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        String jwtAccessToken = jwtService.generateJwtToken(userDetails, true);
        return JWTTokenDTO.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(refreshToken) // return same refresh token
                .build();
    }
}
