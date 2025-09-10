package com.tushar.taskmanagerapp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tushar.taskmanagerapp.dto.Response;
import com.tushar.taskmanagerapp.dto.UserRequest.EmailVerificationRequest;
import com.tushar.taskmanagerapp.dto.UserRequest.LoginRequest;
import com.tushar.taskmanagerapp.dto.UserRequest.PasswordResetRequest;
import com.tushar.taskmanagerapp.dto.UserRequest.PerformEmailVerification;
import com.tushar.taskmanagerapp.dto.UserRequest.PerformResetRequest;
import com.tushar.taskmanagerapp.dto.UserRequest.RegisterRequest;
import com.tushar.taskmanagerapp.model.PasswordResetToken;
import com.tushar.taskmanagerapp.model.User;
import com.tushar.taskmanagerapp.service.EmailService;
import com.tushar.taskmanagerapp.service.UserService;
import com.tushar.taskmanagerapp.service.implementation.EmailVerificationService;
import com.tushar.taskmanagerapp.service.implementation.PasswordResetService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailVerificationService emailVerificationService;
    @Autowired
    private PasswordResetService passwordResetService;
    @Autowired
    EmailService emailService;


    @PostMapping("/register")
    public ResponseEntity<Response<?>> signUp(@Valid @RequestBody RegisterRequest userRequest) {
        return ResponseEntity.ok(userService.signUp(userRequest));
    }
    
    @PostMapping("/login")
    public ResponseEntity<Response<?>> login(@Valid @RequestBody LoginRequest userRequest) {
        return ResponseEntity.ok(userService.login(userRequest));
    }

    @PostMapping("/request-verification")
    public ResponseEntity<?> requestVerification(@Validated @RequestBody EmailVerificationRequest request) {
        Optional<User> userOpt = userService.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        emailVerificationService.sendEmailVerificationToken(userOpt.get());
        return ResponseEntity.ok("Verification token sent to " + request.getEmail());
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody PerformEmailVerification token) {
        boolean success = emailVerificationService.verifyEmail(token);
        if (!success) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
        return ResponseEntity.ok("Email verified successfully.");
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<?> requestPasswordReset(@Validated @RequestBody PasswordResetRequest request) {
        Optional<User> userOpt = userService.findByUsernameOrEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        PasswordResetToken token = passwordResetService.createPasswordResetToken(userOpt.get().getId());

        String body = "Use this token to reset your password:\n\n" + token.getToken() +
        "\n\nThis token expires at: " + token.getExpiresAt() + " UTC";
        emailService.send(request.getEmail(), "Password Reset.", body);
        return ResponseEntity.ok("Password reset token sent to " + request.getEmail());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Validated @RequestBody PerformResetRequest request) {
        boolean success = passwordResetService.resetPassword(request.getToken(), request.getNewPassword(), request.getUsername());
        if (!success) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
        return ResponseEntity.ok("Password has been reset successfully.");
    }
}
