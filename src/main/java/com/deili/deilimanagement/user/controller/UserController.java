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

import java.util.List;
import java.util.Optional;

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

    @QueryMapping
    public Optional<User> getFindByEmail(@Argument String email) {
        return userService.findByEmail(email);
    }

    @QueryMapping
    public List<User> getSearchUserByEmail(@Argument String email) {
        return userService.searchUserByEmail(email);
    }

    @QueryMapping
    public List<?> getAllUser(){
        return userService.getAllUser();
    }

    @MutationMapping
    public User updateUserProfile(@Argument("input") UpdateProfileDto updateProfileDto) {
        return userService.updateUserProfile(updateProfileDto);
    }

    @MutationMapping
    public String resendVerificationEmail(@Argument Long userId) {
        userService.resendVerificationEmail(userId);
        return "Verification email has been resent.";
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