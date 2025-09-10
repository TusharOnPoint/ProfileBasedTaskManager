package com.tushar.taskmanagerapp.repository;

import com.tushar.taskmanagerapp.enums.Roles;
import com.tushar.taskmanagerapp.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setFullname(rs.getString("fullname"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(Roles.valueOf(rs.getString("role")));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        user.setEmailVerified(rs.getBoolean("email_verified"));
        return user;
    };

    public Optional<User> findByUsername(String username) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE username=?", userMapper, username);
        return users.stream().findFirst();
    }

    public Optional<User> findById(Long id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", userMapper, id);
        return users.stream().findFirst();
    }

    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username=? OR email=?", userMapper,
                usernameOrEmail, usernameOrEmail).stream().findFirst();
    }

    public User save(User user) {
        String sql = "INSERT INTO users (username, fullname, password, role, created_at, updated_at, email, email_verified) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFullname());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole().name());
            ps.setObject(5, user.getCreatedAt());
            ps.setObject(6, user.getUpdatedAt());
            ps.setString(7, user.getEmail());
            ps.setBoolean(8, user.getEmailVerified());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            user.setId(keyHolder.getKey().longValue());
        }
        return user;
    }

    public void markUserAsVerified(Long userId) {
        String sql = "UPDATE users SET email_verified = TRUE WHERE id = ?";
        jdbcTemplate.update(sql, userId);
    }

    public void updatePassword(Long userId, String hashedPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        jdbcTemplate.update(sql, hashedPassword, userId);
    }

    public List<User> findAll() {
        String sql = "SELECT id, created_at, fullname, username, email, password, role, updated_at, email_verified FROM users";
        return jdbcTemplate.query(sql, userMapper);
    }

    public void updateRole(Long userId, Roles role) {
        String sql = "UPDATE users SET role = ? WHERE id = ?";
        jdbcTemplate.update(sql, role.name(), userId);
    }

    public void deleteById(Long userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, userId);
    }

}
