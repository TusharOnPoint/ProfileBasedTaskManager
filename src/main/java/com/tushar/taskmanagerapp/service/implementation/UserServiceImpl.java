package com.tushar.taskmanagerapp.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tushar.taskmanagerapp.dto.Response;
import com.tushar.taskmanagerapp.dto.UserRequest;
import com.tushar.taskmanagerapp.dto.UserRequest.LoginRequest;
import com.tushar.taskmanagerapp.dto.UserRequest.RegisterRequest;
import com.tushar.taskmanagerapp.enums.Roles;
import com.tushar.taskmanagerapp.exceptions.BadRequestException;
import com.tushar.taskmanagerapp.exceptions.NotFoundException;
import com.tushar.taskmanagerapp.model.EmailVerificationToken;
import com.tushar.taskmanagerapp.model.User;
import com.tushar.taskmanagerapp.repository.UserRepository;
import com.tushar.taskmanagerapp.security.JwtUtils;
import com.tushar.taskmanagerapp.service.EmailService;
import com.tushar.taskmanagerapp.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public Response<?> signUp(RegisterRequest userRequest) {
        log.info("Inside signUp()");
        Optional<User> existingUser = userRepository.findByUsername(userRequest.getUsername());

        if (existingUser.isPresent()) {
            throw new BadRequestException("Username already taken.");
        }

        User user = new User();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole(userRequest.getRole() == null || userRequest.getRole().isEmpty() ? Roles.USER
                : Roles.valueOf(userRequest.getRole().toUpperCase()));
        // user.setRole(Roles.USER);
        user.setUsername(userRequest.getUsername());
        user.setFullname(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setEmailVerified(false);

        User savedUser = userRepository.save(user);
        emailVerificationService.sendEmailVerificationToken(savedUser);

        return Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("User registered successfully. Verification token sent to email.")
                .build();
    }

    @Override
    public Response<?> login(LoginRequest userRequest) {
        log.info("Inside login()");
        User user = userRepository.findByUsernameOrEmail(userRequest.getUsernameOrEmail())
                .orElseThrow(() -> new NotFoundException("User not found."));
        if (!user.getEmailVerified())
            throw new BadRequestException("Your email is not verified. Please verify your email to login.");
        if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid password.");
        }
        String token = jwtUtils.generateToken(user.getUsername());
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("login successfully.")
                .data(token)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found."));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail);
    }

    
}
