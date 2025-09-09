package com.tushar.taskmanagerapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tushar.taskmanagerapp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameK(String username);
}
