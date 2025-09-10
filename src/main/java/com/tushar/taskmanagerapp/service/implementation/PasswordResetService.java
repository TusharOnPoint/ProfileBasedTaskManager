package com.tushar.taskmanagerapp.service.implementation;

import com.tushar.taskmanagerapp.model.PasswordResetToken;
import com.tushar.taskmanagerapp.model.User;
import com.tushar.taskmanagerapp.repository.PasswordResetTokenRepository;
import com.tushar.taskmanagerapp.repository.UserRepository;
import com.tushar.taskmanagerapp.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetToken createPasswordResetToken(Long userId) {
        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(1))
                .used(false)
                .build();

        tokenRepository.save(resetToken);
        return resetToken;
    }

    public boolean resetPassword(String token, String newPassword, String username) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);

        if (tokenOpt.isEmpty()) {
            return false;
        }

        PasswordResetToken resetToken = tokenOpt.get();

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now()) || Boolean.TRUE.equals(resetToken.getUsed())
                        || resetToken.getUserId()!=userService.findByUsername(username).get().getId()) {
            return false;
        }

        String hashedPassword = passwordEncoder.encode(newPassword);
        userRepository.updatePassword(resetToken.getUserId(), hashedPassword);

        tokenRepository.markAsUsed(resetToken.getId());

        return true;
    }
}

