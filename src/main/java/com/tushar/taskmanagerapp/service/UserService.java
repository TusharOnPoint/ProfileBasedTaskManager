package com.tushar.taskmanagerapp.service;

import com.tushar.taskmanagerapp.dto.Response;
import com.tushar.taskmanagerapp.dto.UserRequest;
import com.tushar.taskmanagerapp.model.User;

public interface UserService {
    Response<?> signUp(UserRequest userRequest);
    Response<?> login(UserRequest userRequest);
    User getCurrentLoggedInUser();
}
