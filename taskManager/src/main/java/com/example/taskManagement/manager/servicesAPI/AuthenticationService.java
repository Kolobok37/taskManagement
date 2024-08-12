package com.example.taskManagement.manager.servicesAPI;

import com.example.taskManagement.manager.dto.JwtAuthenticationResponse;
import com.example.taskManagement.manager.dto.SignInRequest;
import com.example.taskManagement.manager.dto.SignUpRequest;

public interface AuthenticationService {
    public JwtAuthenticationResponse signUp(SignUpRequest request);
    JwtAuthenticationResponse signIn(SignInRequest request) ;
}
