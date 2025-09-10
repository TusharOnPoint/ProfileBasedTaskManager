package com.tushar.taskmanagerapp.repository;

import com.tushar.taskmanagerapp.model.EmailVerificationToken;
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
public class EmailVerificationTokenRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<EmailVerificationToken> rowMapper = (rs, rowNum) -> EmailVerificationToken.builder()
            .id(rs.getLong("id"))
            .token(rs.getString("token"))
            .userId(rs.getLong("user_id"))
            .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
            .expiresAt(rs.getTimestamp("expires_at").toLocalDateTime())
            .build();

    public EmailVerificationToken save(EmailVerificationToken token) {
        String sql = "INSERT INTO email_verification_tokens (token, user_id, created_at, expires_at) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, token.getToken());
            ps.setLong(2, token.getUserId());
            ps.setObject(3, token.getCreatedAt() != null ? token.getCreatedAt() : LocalDateTime.now());
            ps.setObject(4, token.getExpiresAt());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            token.setId(keyHolder.getKey().longValue());
        }
        return token;
    }

    public Optional<EmailVerificationToken> findByToken(String token) {
        String sql = "SELECT * FROM email_verification_tokens WHERE token = ?";
        return jdbcTemplate.query(sql, rowMapper, token).stream().findFirst();
    }

    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM email_verification_tokens WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}
