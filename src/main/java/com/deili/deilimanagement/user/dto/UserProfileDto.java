package com.deili.deilimanagement.user.dto;

import lombok.Data;

@Data
public class UserProfileDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String jobRole;
}
