package com.tushar.taskmanagerapp.repository;

import com.tushar.taskmanagerapp.model.PasswordResetToken;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PasswordResetTokenRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<PasswordResetToken> rowMapper = (rs, rowNum) -> PasswordResetToken.builder()
            .id(rs.getLong("id"))
            .token(rs.getString("token"))
            .userId(rs.getLong("user_id"))
            .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
            .expiresAt(rs.getTimestamp("expires_at").toLocalDateTime())
            .used(rs.getBoolean("used"))
            .build();

    public PasswordResetToken save(PasswordResetToken token) {
        String sql = "INSERT INTO password_reset_tokens (token, user_id, created_at, expires_at, used) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, token.getToken());
            ps.setLong(2, token.getUserId());
            ps.setObject(3, token.getCreatedAt() != null ? token.getCreatedAt() : LocalDateTime.now());
            ps.setObject(4, token.getExpiresAt());
            ps.setBoolean(5, token.getUsed() != null ? token.getUsed() : false);
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            token.setId(keyHolder.getKey().longValue());
        }
        return token;
    }

    public Optional<PasswordResetToken> findByToken(String token) {
        String sql = "SELECT * FROM password_reset_tokens WHERE token = ?";
        return jdbcTemplate.query(sql, rowMapper, token).stream().findFirst();
    }

    public void markAsUsed(Long id) {
        String sql = "UPDATE password_reset_tokens SET used = TRUE WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM password_reset_tokens WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}
