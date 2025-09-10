package com.tushar.taskmanagerapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {

    @Data
    public static class RegisterRequest {
        @NotBlank
        @Size(min = 3, max = 50)
        private String username;

        @Email
        @NotBlank
        private String email;

        @NotBlank
        @Size(min = 6, max = 100)
        private String password;
        private String role;
    }

    @Data
    public static class LoginRequest {
        @NotBlank
        private String usernameOrEmail;

        @NotBlank
        private String password;
    }

    @Data
    public static class LoginResponse {
        private String token;
        private Long userId;
        private String username;
        private String role;
    }

    @Data
    public static class PasswordResetRequest {
        @Email
        @NotBlank
        private String email;
    }

    @Data
    public static class PerformResetRequest {
        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Token is required")
        private String token;

        @NotBlank(message = "New password cannot be empty")
        @Size(min = 6, max = 100)
        private String newPassword;
    }

    @Data
    public static class EmailVerificationRequest {
        @NotBlank(message = "Username is required.")
        private String username;
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required.")
        private String email;
    }

    @Data
    public static class PerformEmailVerification {
        @NotBlank
        private String token;
        @NotBlank
        private String username;
    }

}
