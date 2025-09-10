package com.tushar.taskmanagerapp.service.implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tushar.taskmanagerapp.dto.Response;
import com.tushar.taskmanagerapp.dto.TaskRequest;
import com.tushar.taskmanagerapp.enums.Priority;
import com.tushar.taskmanagerapp.exceptions.NotFoundException;
import com.tushar.taskmanagerapp.model.Task;
import com.tushar.taskmanagerapp.model.User;
import com.tushar.taskmanagerapp.repository.TaskRepository;
import com.tushar.taskmanagerapp.service.TaskService;
import com.tushar.taskmanagerapp.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;
    private final UserService userService;
    @Override
    public Response<Task> createTask(TaskRequest taskRequest) {
        log.info("Inside create task().");
        User user = userService.getCurrentLoggedInUser();
        Task taskToSave = Task.builder()
                                .title(taskRequest.getTitle())  
                                .description(taskRequest.getDescription())  
                                .completed(taskRequest.getCompleted())  
                                .priority(taskRequest.getPriority())
                                .dueDate(taskRequest.getDueDate())
                                .createdAt(LocalDateTime.now())
                                .updateddAt(LocalDateTime.now())
                                .user(user)
                                .build();
        Task savedTask = taskRepository.save(taskToSave);
        return Response.<Task>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Task created successfully.")
                        .data(savedTask)
                        .build();
    }

    @Override
    @Transactional
    public Response<List<Task>> getAllMyTasks() {
        log.info("Inside create getAllMyTasks().");
        User user = userService.getCurrentLoggedInUser();
        List<Task> tasks = taskRepository.findByUser(user, Sort.by(Sort.Direction.DESC, "id"));
        return Response.<List<Task>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Tasks retreived successfully.")
                        .data(tasks)
                        .build();
    }

    @Override
    public Response<Task> getTaskById(Long id) {
        log.info("Inside create getTaskById().");
        Task task = taskRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Task not found."));
        return Response.<Task>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Task retreived successfully.")
                        .data(task)
                        .build();
    }

    @Override
    public Response<Task> updateTask(TaskRequest taskRequest) {
        log.info("Inside create updateTask().");
        Task task = taskRepository.findById(taskRequest.getId())
                                .orElseThrow(() -> new NotFoundException("Task not found."));

        if(taskRequest.getTitle()!=null) task.setTitle(taskRequest.getTitle());
        if(taskRequest.getDescription()!=null) task.setDescription(taskRequest.getDescription());
        if(taskRequest.getCompleted()!=null) task.setCompleted(taskRequest.getCompleted());
        if(taskRequest.getPriority()!=null) task.setPriority(taskRequest.getPriority());
        if(taskRequest.getDueDate()!=null) task.setDueDate(taskRequest.getDueDate());
        task.setUpdateddAt(LocalDateTime.now());

        Task updatedTask = taskRepository.save(task);
        return Response.<Task>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Task updated successfully.")
                        .data(updatedTask)
                        .build();
    }

    @Override
    public Response<Void> deleteTask(Long id) {
        log.info("Inside create deleteTask().");

        if(!taskRepository.existsById(id))
            throw new NotFoundException("Task does not exist.");
        
        taskRepository.deleteById(id);
        return Response.<Void>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Task deleted successfully.")
                        .build();
    }

    @Override
    @Transactional
    public Response<List<Task>> getMyTasksByCompletedStatus(boolean completed) {
        log.info("Inside create getMyTasksByCompletedStatus().");
        User user = userService.getCurrentLoggedInUser();
        List<Task> tasks = taskRepository.findByCompletedAndUser(completed, user);
        return Response.<List<Task>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Tasks filtered by completed status for user.")
                        .data(tasks)
                        .build();
    }

    @Override
    public Response<List<Task>> getMyTasksByPriority(String priority) {
        log.info("Inside create getMyTasksByPriority().");
        User user = userService.getCurrentLoggedInUser();
        Priority priorityEnum = Priority.valueOf(priority.toUpperCase());
        List<Task> tasks = taskRepository.findByPriorityAndUser(priorityEnum, user, 
                                        Sort.by(Sort.Direction.DESC, "id"));
        return Response.<List<Task>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Tasks filtered by priority for user.")
                        .data(tasks)
                        .build();
    }
    
}
