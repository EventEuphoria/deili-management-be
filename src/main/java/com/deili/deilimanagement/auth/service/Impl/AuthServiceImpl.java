package com.deili.deilimanagement.auth.service.Impl;

import com.deili.deilimanagement.auth.dto.LoginResponseDto;
import com.deili.deilimanagement.auth.helper.Claims;
import com.deili.deilimanagement.auth.repository.AuthRedisRepository;
import com.deili.deilimanagement.auth.service.AuthService;
import com.deili.deilimanagement.exception.ResourceNotFoundException;
import com.deili.deilimanagement.user.entity.User;
import com.deili.deilimanagement.user.repository.UserRepository;
import com.deili.deilimanagement.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final JwtEncoder jwtEncoder;
    private final AuthRedisRepository authRedisRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponseDto generateToken(Authentication authentication) {

        Optional<User> userOptional = userService.findByEmail(authentication.getName());
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        User user = userOptional.get();
        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .build();

        String jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        if (authRedisRepository.isKeyBlacklisted(jwt)) {
            throw new ResourceNotFoundException("JWT Token has already been blacklisted");
        }

        authRedisRepository.saveJwtKey(authentication.getName(), jwt);

        return new LoginResponseDto();
    }

    @Override
    public void logout(String token) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");

        if (authRedisRepository.isKeyBlacklisted(token)) {
            throw new ResourceNotFoundException("JWT Token has already been blacklisted");
        }
        authRedisRepository.blackListJwt(email, token);
        authRedisRepository.deleteJwtKey(email);
    }
}