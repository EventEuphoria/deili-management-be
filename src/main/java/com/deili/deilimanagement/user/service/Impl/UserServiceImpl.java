package com.deili.deilimanagement.user.service.Impl;

import com.deili.deilimanagement.user.dto.*;
import com.deili.deilimanagement.user.entity.JobRole;
import com.deili.deilimanagement.user.entity.User;
import com.deili.deilimanagement.user.repository.JobRoleRepository;
import com.deili.deilimanagement.user.repository.UserRepository;
import com.deili.deilimanagement.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final JobRoleRepository jobRoleRepository;
    private static final Logger log =  LoggerFactory.getLogger(UserServiceImpl.class);;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        try {
            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail().toLowerCase());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setPhoneNumber(request.getPhoneNumber());

            log.info("Fetching JobRole with ID: {}", request.getJobRoleId());

            JobRole jobRole = jobRoleRepository.findById(request.getJobRoleId())
                    .orElseThrow(() -> new IllegalArgumentException("Job Role not found"));

            log.info("Fetched JobRole: {}", jobRole);

            user.setJobRole(jobRole);
            user.setRole("user");
            user.setVerified(false);

            User savedUser = userRepository.save(user);
            sendVerificationEmail(savedUser);

            return savedUser;
        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setId(user.getId());
        userProfileDto.setFirstName(user.getFirstName());
        userProfileDto.setLastName(user.getLastName());
        userProfileDto.setEmail(user.getEmail());
        userProfileDto.setPhoneNumber(user.getPhoneNumber());
        userProfileDto.setJobRole(user.getJobRole() != null ? user.getJobRole().getTitle() : null);
        userProfileDto.setIsVerified(user.isVerified());

        return userProfileDto;
    }

    @Override
    public User updateUserProfile(UpdateProfileDto updateProfileDto) {
        User user = userRepository.findById(updateProfileDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean passwordMatches = passwordEncoder.matches(updateProfileDto.getCurrentPassword(), user.getPassword());
        log.info("Password match result for user {}: {}", updateProfileDto.getUserId(), passwordMatches);

        if (!passwordMatches) {
            throw new IllegalArgumentException("Incorrect current password.");
        }

        if (updateProfileDto.getNewPassword() != null && !updateProfileDto.getNewPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateProfileDto.getNewPassword()));
        }

        user.setFirstName(updateProfileDto.getFirstName());
        user.setLastName(updateProfileDto.getLastName());

        if (!user.getEmail().equalsIgnoreCase(updateProfileDto.getEmail())) {
            user.setEmail(updateProfileDto.getEmail().toLowerCase());
            user.setVerified(false);
            sendVerificationEmail(user);
        }

        user.setPhoneNumber(updateProfileDto.getPhoneNumber());

        if (updateProfileDto.getJobRoleId() != null) {
            JobRole jobRole = jobRoleRepository.findById(updateProfileDto.getJobRoleId())
                    .orElseThrow(() -> new IllegalArgumentException("Job Role not found"));
            user.setJobRole(jobRole);
        }

        return userRepository.save(user);
    }

    @Override
    public void sendVerificationEmail(User user) {
        String token = generateToken(user);
        String verificationLink = "http://localhost:3000/verify-email?token=" + token;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Verify Your Email");
        mailMessage.setText("Hello " + user.getFirstName() + user.getLastName() +"! Welcome to Deili Management. Please verify your email using the following link (expires in 1 hour): " + verificationLink);

        mailSender.send(mailMessage);
    }

    @Override
    public void resendVerificationEmail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.isVerified()) {
            throw new IllegalArgumentException("User is already verified.");
        }

        sendVerificationEmail(user);
    }

    @Override
    public void verifyUserEmail(String token) {
        try {
            String[] tokenData = decodeToken(token);
            String email = tokenData[1];
            Instant expiryDate = Instant.parse(tokenData[2]);

            if (Instant.now().isAfter(expiryDate)) {
                throw new IllegalArgumentException("Token expired. Please request a new verification link.");
            }

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            if (user.isVerified()) {
                throw new IllegalArgumentException("User is already verified.");
            }

            user.setVerified(true);
            userRepository.save(user);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = generateToken(user);
        String resetLink = "http://localhost:3000/reset-password?token=" + token;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("Deili Management asking to reset password. Are you request to reset password? If so, please click the link to reset your password: " + resetLink + ". If not please ignore this message.");

        mailSender.send(mailMessage);
    }

    @Override
    public void resetPassword(String email, String token, String newPassword) {
        User user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!validateToken(token, user)) {
            throw new IllegalArgumentException("Invalid or expired token.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private String generateToken(User user) {
        String token = UUID.randomUUID().toString();
        String combined = token + "|" + user.getEmail() + "|" + Instant.now().plusSeconds(3600);
        return Base64.getEncoder().encodeToString(combined.getBytes());
    }

    private boolean validateToken(String token, User user) {
        String[] tokenData = decodeToken(token);
        return tokenData[1].equals(user.getEmail()) && Instant.parse(tokenData[2]).isAfter(Instant.now());
    }

    private String[] decodeToken(String token) {
        String decoded = new String(Base64.getDecoder().decode(token));
        return decoded.split("\\|");
    }
}
