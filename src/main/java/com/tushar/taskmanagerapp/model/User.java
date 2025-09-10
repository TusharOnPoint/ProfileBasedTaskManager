package com.tushar.taskmanagerapp.model;

import java.time.LocalDateTime;

import com.tushar.taskmanagerapp.enums.Roles;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;

    @NotBlank(message = "Username is required.")
    private String username;

    private String fullname;

    @Email
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    private Roles role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Getter @Setter
    private boolean emailVerified;

    public boolean getEmailVerified(){
        return emailVerified;
    }
}
