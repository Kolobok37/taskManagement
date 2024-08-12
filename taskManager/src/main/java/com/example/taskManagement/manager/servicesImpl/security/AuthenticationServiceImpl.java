package com.example.taskManagement.manager.servicesImpl.security;

import com.example.taskManagement.manager.dto.JwtAuthenticationResponse;
import com.example.taskManagement.manager.dto.SignInRequest;
import com.example.taskManagement.manager.dto.SignUpRequest;
import com.example.taskManagement.manager.entities.Role;
import com.example.taskManagement.manager.entities.User;
import com.example.taskManagement.manager.servicesAPI.AuthenticationService;
import com.example.taskManagement.manager.servicesAPI.JwtService;
import com.example.taskManagement.manager.servicesAPI.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Регилстрация пользователя
     *      *
     *      * @param request данные поьзователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        User userCreated = userService.create(user);

        var jwt = jwtService.generateToken(userCreated);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
