package com.test.shedlocktest.services.impl;

import com.test.shedlocktest.services.JdbcLockSingleKeyService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Data
@Slf4j
@Service
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class JdbcLockSingleKeyServiceImpl implements JdbcLockSingleKeyService {
    public static final String ACQUIRE_FORMATTED_QUERY = "INSERT INTO shedlock (name, lock_until, locked_at, locked_by) VALUES (?, ?, ?, ?)";
    public static final String RELEASE_FORMATTED_QUERY = "DELETE FROM shedlock WHERE name = ?";
    public static final String DELETE_EXPIRED_FORMATTED_QUERY = "DELETE FROM shedlock WHERE lock_until < ?";
    public static final String REFRESH_FORMATTED_QUERY = "UPDATE shedlock SET lock_until = ? WHERE name = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public String acquire(String name, long expiration) {
        final Date now = new Date();
        final int expired = jdbcTemplate.update(DELETE_EXPIRED_FORMATTED_QUERY, now);
        log.debug("Expired {} locks", expired);
        try {
            final Date expireAt = new Date(now.getTime() + expiration);
            final int created = jdbcTemplate.update(ACQUIRE_FORMATTED_QUERY, name, expireAt, now, "JIMMAR9");
            log.debug("Created {} locks", created);
            return created == 1 ? name : null;

        } catch (DuplicateKeyException e) {
            log.error("Error while creating lock '{}'", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean release(String name) {
        final int deleted = jdbcTemplate.update(RELEASE_FORMATTED_QUERY, name);
        final boolean released = deleted == 1;
        log.debug("Released lock: {}", released);
        if (released) {
            log.debug("Lock released: {}", name);
        } else if (deleted > 0) {
            log.error("Released more than one lock: {}", deleted);
        } else {
            log.error("Lock not released: {}", name);
        }
        return released;
    }

    @Override
    public boolean refresh(String name, long expiration) {
        final Date now = new Date();
        log.info("Now: {}", now);
        final Date expireAt = new Date(now.getTime() + expiration);
        log.info("ExpireAt: {}", expireAt);
        final int updated = jdbcTemplate.update(REFRESH_FORMATTED_QUERY, expireAt, name);
        final boolean refreshed = updated == 1;
        log.debug("Refreshed lock: {}", refreshed);

        if (refreshed) {
            log.info("Lock refreshed: {}", name);
        } else if (updated > 0) {
            log.error("Refreshed more than one lock: {}", updated);
        } else {
            log.error("Lock not refreshed: {}", name);
        }
        return refreshed;
    }
}
