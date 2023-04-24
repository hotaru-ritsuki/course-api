package com.example.courseapi.security.controller;

import com.example.courseapi.security.dto.JWTRefreshDTO;
import com.example.courseapi.security.dto.LoginRequestDTO;
import com.example.courseapi.security.dto.JWTTokenDTO;
import com.example.courseapi.security.dto.SignUpRequestDTO;
import com.example.courseapi.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SecurityRequirements
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<JWTTokenDTO> register(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        return ResponseEntity.ok(authenticationService.register(signUpRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<JWTTokenDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        log.info("POST request to login user with username : {}", loginRequestDTO.getEmail());
        return ResponseEntity.ok(authenticationService.authenticate(loginRequestDTO));
    }

    @GetMapping("/refresh")
    public ResponseEntity<JWTTokenDTO> refreshToken(@RequestBody JWTRefreshDTO jwtRefreshDTO, HttpServletRequest request) {
        return ResponseEntity.ok(authenticationService.refresh(jwtRefreshDTO, request));
    }

}
