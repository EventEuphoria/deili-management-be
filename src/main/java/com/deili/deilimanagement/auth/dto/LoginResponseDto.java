package com.deili.deilimanagement.auth.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String accessToken;
    private String userId;
    private String email;
    private String role;
}