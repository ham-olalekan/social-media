package com.prophius.socialmediaservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.prophius.socialmediaservice.util.Constants.EntityColumns.TOKEN_STORE;

@Service
@Transactional
@RequiredArgsConstructor
public class IJwtService {

    private final JdbcTemplate jdbcTemplate;

    @Async
    public void save(long userId, String uuid) {
        String query = "INSERT INTO " + TOKEN_STORE + "(user_id, uuid, enabled) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, userId, uuid, true);
    }

    @Deprecated
    @Async
    public void revokeAllTokensForUser(long userId) {
        String query = "UPDATE " + TOKEN_STORE + " SET enabled=false WHERE user_id=?";
        jdbcTemplate.update(query, userId);
    }

    public boolean isTokenEnabled(long userId, String uuid) {
        String query = "SELECT enabled FROM " + TOKEN_STORE + " WHERE user_id=? AND uuid=?";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, userId, uuid));
    }
}