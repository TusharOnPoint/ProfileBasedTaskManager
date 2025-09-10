package com.tushar.taskmanagerapp.repository;

import com.tushar.taskmanagerapp.enums.Priority;
import com.tushar.taskmanagerapp.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Task> taskMapper = (rs, rowNum) -> Task.builder()
            .id(rs.getLong("id"))
            .title(rs.getString("title"))
            .description(rs.getString("description"))
            .completed(rs.getBoolean("completed"))
            .priority(Priority.valueOf(rs.getString("priority")))
            .dueDate(rs.getTimestamp("due_date") != null ? rs.getTimestamp("due_date").toLocalDateTime() : null)
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .updatedAt(rs.getTimestamp("updatedd_at").toLocalDateTime())
            .userId(rs.getLong("user_id"))
            .build();

    public Task save(Task task) {
    String sql = "INSERT INTO tasks (title, description, completed, priority, due_date, created_at, updatedd_at, user_id) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, task.getTitle());
        ps.setString(2, task.getDescription());
        ps.setBoolean(3, task.getCompleted());
        ps.setString(4, task.getPriority() != null ? task.getPriority().name() : null);
        ps.setObject(5, task.getDueDate());
        ps.setObject(6, task.getCreatedAt());
        ps.setObject(7, task.getUpdatedAt());
        ps.setLong(8, task.getUserId());
        return ps;
    }, keyHolder);

    Number key = keyHolder.getKey();
    if (key != null) {
        task.setId(key.longValue());
    }

    return task;
}

    public List<Task> findByUser(Long userId) {
        return jdbcTemplate.query("SELECT * FROM tasks WHERE user_id=? ORDER BY id DESC", taskMapper, userId);
    }

    public List<Task> findByCompletedAndUser(boolean completed, Long userId) {
        return jdbcTemplate.query("SELECT * FROM tasks WHERE completed=? AND user_id=?", taskMapper, completed, userId);
    }

    public List<Task> findByPriorityAndUser(Priority priority, Long userId) {
        return jdbcTemplate.query("SELECT * FROM tasks WHERE priority=? AND user_id=? ORDER BY id DESC",
                taskMapper, priority.name(), userId);
    }

    public Optional<Task> findById(Long id) {
        List<Task> tasks = jdbcTemplate.query("SELECT * FROM tasks WHERE id=?", taskMapper, id);
        return tasks.stream().findFirst();
    }

    public int update(Task task) {
        return jdbcTemplate.update(
                "UPDATE tasks SET title=?, description=?, completed=?, priority=?, due_date=?, updatedd_at=? WHERE id=?",
                task.getTitle(), task.getDescription(), task.getCompleted(),
                task.getPriority().name(), task.getDueDate(), task.getUpdatedAt(), task.getId()
        );
    }

    public int delete(Long id) {
        return jdbcTemplate.update("DELETE FROM tasks WHERE id=?", id);
    }

    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tasks WHERE id=?", Integer.class, id);
        return count != null && count > 0;
    }
}
