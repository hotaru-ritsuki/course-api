package com.example.courseapi.security.service.impl;

import com.example.courseapi.domain.User;
import com.example.courseapi.domain.enums.Roles;
import com.example.courseapi.security.dto.AuthenticationRequest;
import com.example.courseapi.security.dto.AuthenticationResponse;
import com.example.courseapi.security.dto.RegisterRequest;
import com.example.courseapi.exception.UserNotFoundException;
import com.example.courseapi.repository.UserRepository;
import com.example.courseapi.security.service.AuthenticationService;
import com.example.courseapi.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getFirstName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Roles.STUDENT)
                .build();
        userRepository.save(user);
        String jwtToken = jwtService.generateJwtToken(user);
        return AuthenticationResponse.builder().jwtToken(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        log.info("Attempt to login user with username : {}", authenticationRequest.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(UserNotFoundException::new);
        String jwtToken = jwtService.generateJwtToken(user);
        return AuthenticationResponse.builder().jwtToken(jwtToken).build();
    }
}
