package com.example.courseapi.security.service;

import com.example.courseapi.security.dto.AuthenticationRequest;
import com.example.courseapi.security.dto.AuthenticationResponse;
import com.example.courseapi.security.dto.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest registerRequest);

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
