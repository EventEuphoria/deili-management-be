package com.deili.deilimanagement.user.service;

import com.deili.deilimanagement.user.dto.RegistrationRequest;
import com.deili.deilimanagement.user.dto.UpdateProfileDto;
import com.deili.deilimanagement.user.dto.UserProfileDto;
import com.deili.deilimanagement.user.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
    User registerUser(RegistrationRequest request);
    UserProfileDto getUserProfile(Long userId);
    User updateUserProfile(UpdateProfileDto updateProfileDto);
    void sendVerificationEmail(User user);
    void resendVerificationEmail(Long userId);
    void verifyUserEmail(String token);
    void requestPasswordReset(String email);
    void resetPassword(String email, String token, String newPassword);
}
