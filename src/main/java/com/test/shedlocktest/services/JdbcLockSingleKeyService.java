package com.test.shedlocktest.services;

public interface JdbcLockSingleKeyService {
    String acquire(String name, long expiration);

    boolean release(String name);

    boolean refresh(String name, long expiration);
}
