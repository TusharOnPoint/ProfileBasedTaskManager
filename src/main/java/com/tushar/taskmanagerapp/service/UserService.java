package com.tushar.taskmanagerapp.service;

import java.util.List;
import java.util.Optional;

import com.tushar.taskmanagerapp.dto.Response;
import com.tushar.taskmanagerapp.dto.UserRequest;
import com.tushar.taskmanagerapp.dto.UserRequest.LoginRequest;
import com.tushar.taskmanagerapp.dto.UserRequest.RegisterRequest;
import com.tushar.taskmanagerapp.enums.Roles;
import com.tushar.taskmanagerapp.model.User;

public interface UserService {
    Response<?> signUp(RegisterRequest userRequest);
    Response<?> login(LoginRequest userRequest);
    User getCurrentLoggedInUser();
    public Optional<User> findByUsername(String username);
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail);

    List<User> getAllUsers();
    Response<?> changeUserRole(Long userId, Roles newRole);
    Response<?> deleteUserById(Long userId);
}
