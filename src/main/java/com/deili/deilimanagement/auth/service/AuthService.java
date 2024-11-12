package com.deili.deilimanagement.auth.service;

import com.deili.deilimanagement.auth.dto.LoginRequestDto;
import com.deili.deilimanagement.auth.dto.LoginResponseDto;
import org.springframework.security.core.Authentication;

public interface AuthService {
    LoginResponseDto generateToken(Authentication authentication);
    void logout(String token);

}
