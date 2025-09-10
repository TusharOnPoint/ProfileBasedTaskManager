package com.tushar.taskmanagerapp.service.implementation;

import com.tushar.taskmanagerapp.dto.UserRequest.PerformEmailVerification;
import com.tushar.taskmanagerapp.exceptions.NotFoundException;
import com.tushar.taskmanagerapp.model.EmailVerificationToken;
import com.tushar.taskmanagerapp.model.User;
import com.tushar.taskmanagerapp.repository.EmailVerificationTokenRepository;
import com.tushar.taskmanagerapp.repository.UserRepository;
import com.tushar.taskmanagerapp.service.EmailService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public EmailVerificationToken generateVerificationToken(Long userId) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .token(token)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();

        tokenRepository.save(verificationToken);
        return verificationToken;
    }

    public boolean verifyEmail(PerformEmailVerification token) {
        Optional<EmailVerificationToken> tokenOpt = tokenRepository.findByToken(token.getToken());

        if (tokenOpt.isEmpty()) {
            return false;
        }

        EmailVerificationToken verificationToken = tokenOpt.get();
        String username = token.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found."));

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now()) || verificationToken.getUserId()!=user.getId()) {
            return false;
        }

        userRepository.markUserAsVerified(user.getId());

        // Remove used token
        tokenRepository.deleteByUserId(verificationToken.getUserId());

        return true;
    }

    public void sendEmailVerificationToken(User user) {
        EmailVerificationToken token = generateVerificationToken(user.getId());

        String body = "Use this token to verify your email:\n\n" + token.getToken() +
                "\n\nThis token expires at: " + token.getExpiresAt() + " UTC";

        emailService.send(user.getEmail(), "Verify your email.", body);
    }

}

