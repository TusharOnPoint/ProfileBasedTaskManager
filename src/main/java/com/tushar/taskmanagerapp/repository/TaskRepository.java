package com.tushar.taskmanagerapp.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tushar.taskmanagerapp.enums.Priority;
import com.tushar.taskmanagerapp.model.Task;
import com.tushar.taskmanagerapp.model.User;

public interface TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findByUser(User user, Sort sort); 
    List<Task> findByCompletedAndUser(boolean completed, User user);
    List<Task> findByPriorityAndUser(Priority priority, User user, Sort sort);
}
