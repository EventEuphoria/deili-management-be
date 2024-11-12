package com.deili.deilimanagement.user.controller;

import com.deili.deilimanagement.user.dto.*;
import com.deili.deilimanagement.user.entity.User;
import com.deili.deilimanagement.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @MutationMapping
    public User registerUser(@Argument RegistrationRequest request) {
        return userService.registerUser(request);
    }

    @QueryMapping
    public UserProfileDto getUserProfile(@Argument Long userId) {
        return userService.getUserProfile(userId);
    }

    @MutationMapping
    public User updateUserProfile(@Argument UpdateProfileDto updateProfileDto) {
        return userService.updateUserProfile(updateProfileDto);
    }

    @MutationMapping
    public String verifyUserEmail(@Argument String token) {
        userService.verifyUserEmail(token);
        return "Email verified successfully.";
    }


    @MutationMapping
    public String requestPasswordReset(@Argument String email) {
        userService.requestPasswordReset(email);
        return "Password reset email sent.";
    }

    @MutationMapping
    public String resetPassword(@Argument String email, @Argument String token, @Argument String newPassword) {
        userService.resetPassword(email, token, newPassword);
        return "Password has been reset.";
    }
}