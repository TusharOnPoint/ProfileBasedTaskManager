package com.tushar.taskmanagerapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tushar.taskmanagerapp.dto.Response;
import com.tushar.taskmanagerapp.dto.TaskRequest;
import com.tushar.taskmanagerapp.model.Task;
import com.tushar.taskmanagerapp.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Response<Task>> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok(taskService.createTask(taskRequest));
    }

    @PutMapping
    public ResponseEntity<Response<Task>> updateTask(@Valid @RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok(taskService.updateTask(taskRequest));
    }

    @GetMapping
    public ResponseEntity<Response<List<Task>>> getAllMyTasks() {
        return ResponseEntity.ok(taskService.getAllMyTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Task>> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteTask(id));
    }

    @GetMapping("/status")
    public ResponseEntity<Response<List<Task>>> getMyTasksByCompletedStatus(@RequestParam boolean completed) {
        return ResponseEntity.ok(taskService.getMyTasksByCompletedStatus(completed));
    }

    @GetMapping("/priority")
    public ResponseEntity<Response<List<Task>>> getMyTasksByPriority(@RequestParam String priority) {
        return ResponseEntity.ok(taskService.getMyTasksByPriority(priority));
    }

    // Admin: fetch all tasks across users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Response<List<Task>>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // Admin: create/assign a task for any user by id
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign/{userId}")
    public ResponseEntity<Response<Task>> createTaskForUser(@PathVariable Long userId,
            @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.createTaskForUser(userId, request));
    }
}
