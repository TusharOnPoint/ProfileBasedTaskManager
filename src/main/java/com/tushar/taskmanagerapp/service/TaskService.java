package com.tushar.taskmanagerapp.service;

import java.util.List;

import com.tushar.taskmanagerapp.dto.Response;
import com.tushar.taskmanagerapp.dto.TaskRequest;
import com.tushar.taskmanagerapp.model.Task;

public interface TaskService {
    Response<Task> createTask(TaskRequest taskRequest);
    Response<List<Task>> getAllMyTasks();
    Response<Task> getTaskById(Long id);
    Response<Task> updateTask(TaskRequest taskRequest);
    Response<Void> deleteTask(Long id);
    Response<List<Task>> getMyTasksByCompletedStatus(boolean completed);
    Response<List<Task>> getMyTasksByPriority(String priority);
    Response<List<Task>> getAllTasks();
    Response<Task> createTaskForUser(Long userId, TaskRequest request);
}
