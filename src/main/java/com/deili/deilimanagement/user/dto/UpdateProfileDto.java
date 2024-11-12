package com.deili.deilimanagement.user.dto;

import lombok.Data;

@Data
public class UpdateProfileDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String currentPassword;
    private String newPassword;
    private String jobRole;
}
