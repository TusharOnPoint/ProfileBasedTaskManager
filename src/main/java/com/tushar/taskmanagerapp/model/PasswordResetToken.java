package com.tushar.taskmanagerapp.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {
    private Long id;

    @NotBlank(message = "Token cannot be empty")
    private String token;

    @NotNull(message = "User ID is required")
    private Long userId;

    private LocalDateTime createdAt;

    @NotNull(message = "Expiry time is required")
    private LocalDateTime expiresAt;

    private Boolean used;
}
