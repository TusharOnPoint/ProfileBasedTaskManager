package com.tushar.taskmanagerapp.model;

import java.time.LocalDateTime;

import com.tushar.taskmanagerapp.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private Long id;

    @NotBlank(message = "Title can not be empty.")
    private String title;

    private String description;

    @NotNull(message = "Completed status is required.")
    private Boolean completed;

    private Priority priority;

    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long userId;
}
