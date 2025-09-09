package com.tushar.taskmanagerapp.dto;

import java.time.LocalDateTime;

import com.tushar.taskmanagerapp.enums.Priority;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequest {
    private Long id;

    @NotBlank(message = "Title can not be empty.")
    @Size(max = 200, message = "Title must be less than 200 characters.")
    private String title;
    @Size(max = 1000, message = "Description must be less than 1000 characters.")
    private String description;

    @NotNull(message = "Completed status is required.")
    private Boolean completed;

    @NotNull(message = "Priority is required.")
    private Priority priority;

    @FutureOrPresent(message = "Due date must be in future or present.")
    private LocalDateTime dueDate;
}
