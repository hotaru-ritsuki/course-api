package com.example.courseapi.security.controller;

import com.example.courseapi.security.dto.JWTRefreshDTO;
import com.example.courseapi.security.dto.LoginRequestDTO;
import com.example.courseapi.security.dto.JWTTokenDTO;
import com.example.courseapi.security.dto.SignUpRequestDTO;
import com.example.courseapi.security.service.AuthenticationService;
//import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@SecurityRequirements
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<Void> register(@Valid @RequestBody final SignUpRequestDTO signUpRequestDTO) {
        log.info("REST POST request to register user : {}", signUpRequestDTO);
        authenticationService.register(signUpRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<JWTTokenDTO> login(
            @Valid @RequestBody final LoginRequestDTO loginRequestDTO, final HttpServletRequest request) {
        log.info("REST POST request to login user with email : {}", loginRequestDTO.getEmail());
        return ResponseEntity.ok(authenticationService.login(loginRequestDTO, request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JWTTokenDTO> refreshAccessToken(
            @Valid @RequestBody final JWTRefreshDTO jwtRefreshDTO, final HttpServletRequest request) {
        log.info("REST POST request to login refresh access token");
        return ResponseEntity.ok(authenticationService.refresh(jwtRefreshDTO, request));
    }

}
