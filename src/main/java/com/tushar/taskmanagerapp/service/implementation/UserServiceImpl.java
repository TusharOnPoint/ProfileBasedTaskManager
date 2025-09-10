package com.tushar.taskmanagerapp.service.implementation;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tushar.taskmanagerapp.dto.Response;
import com.tushar.taskmanagerapp.dto.UserRequest;
import com.tushar.taskmanagerapp.enums.Roles;
import com.tushar.taskmanagerapp.exceptions.BadRequestException;
import com.tushar.taskmanagerapp.exceptions.NotFoundException;
import com.tushar.taskmanagerapp.model.User;
import com.tushar.taskmanagerapp.repository.UserRepository;
import com.tushar.taskmanagerapp.security.JwtUtils;
import com.tushar.taskmanagerapp.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public Response<?> signUp(UserRequest userRequest) {
        log.info("Inside signUp()");
        Optional<User> existingUser = userRepository.findByUsername(userRequest.getUsername());
        
        if(existingUser.isPresent()){
            throw new BadRequestException("Username already taken.");
        }

        User user = new User();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole(Roles.USER);
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        userRepository.save(user);

        return Response.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("User registered successfully.")
                        .build();
    }

    @Override
    public Response<?> login(UserRequest userRequest) {
        log.info("Inside login()");
        User user = userRepository.findByUsername(userRequest.getUsername())
                                    .orElseThrow(()->new NotFoundException("User not found."));
        if(!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())){
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
                    .orElseThrow(()-> new NotFoundException("User not found."));
    }
    
}
