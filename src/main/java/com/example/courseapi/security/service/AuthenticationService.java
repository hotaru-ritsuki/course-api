package com.example.courseapi.security.service;

import com.example.courseapi.security.dto.JWTRefreshDTO;
import com.example.courseapi.security.dto.LoginRequestDTO;
import com.example.courseapi.security.dto.JWTTokenDTO;
import com.example.courseapi.security.dto.SignUpRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    JWTTokenDTO register(SignUpRequestDTO signUpRequestDTO);

    JWTTokenDTO authenticate(LoginRequestDTO loginRequestDTO);

    JWTTokenDTO refresh(JWTRefreshDTO jwtRefreshDTO, HttpServletRequest request);
}
