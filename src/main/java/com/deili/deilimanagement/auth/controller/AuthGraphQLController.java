package com.deili.deilimanagement.auth.controller;


import com.deili.deilimanagement.auth.dto.LoginResponseDto;
import com.deili.deilimanagement.auth.service.AuthService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class AuthGraphQLController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthGraphQLController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @MutationMapping
    public LoginResponseDto login(@Argument String email, @Argument String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        return authService.generateToken(authentication);
    }

    @MutationMapping
    public String logout(@Argument String token) {
        authService.logout(token);
        return "Logout successful";
    }
}