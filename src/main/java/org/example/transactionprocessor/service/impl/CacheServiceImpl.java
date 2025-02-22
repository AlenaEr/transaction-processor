package org.example.transactionprocessor.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.transactionprocessor.service.CacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


/**
 * Implementation of {@link CacheService} that provides caching functionality using Redis.
 * <p>
 * This service caches account balances to reduce database queries and improve performance.
 * It supports saving, retrieving, and deleting balance information.
 */
@Slf4j
@Service
public class CacheServiceImpl implements CacheService {

    private final RedisTemplate<String, BigDecimal> redisTemplate;

    public CacheServiceImpl(RedisTemplate<String, BigDecimal> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Saves the balance of an account in Redis cache.
     *
     * @param accountNumber The account number for which the balance is being saved.
     * @param balance The balance to be saved in the cache.
     */
    @Override
    public void saveBalance(String accountNumber, BigDecimal balance) {
        redisTemplate.opsForValue().set(accountNumber, balance);
        log.info("Balance saved in Redis: {} -> {}", accountNumber, balance);
    }

    /**
     * Retrieves the balance of an account from Redis cache.
     *
     * @param accountNumber The account number for which the balance is requested.
     * @return The balance of the account, or null if not found in the cache.
     */
    @Override
    public BigDecimal getBalance(String accountNumber) {
        BigDecimal balance = redisTemplate.opsForValue().get(accountNumber);
        log.info("Getting balance from Redis: {} -> {}", accountNumber, balance);
        return balance;
    }

    /**
     * Deletes the balance of an account from Redis cache.
     *
     * @param accountNumber The account number for which the balance is being deleted.
     */
    @Override
    public void deleteBalance(String accountNumber) {
        redisTemplate.delete(accountNumber);
        log.info("Balance deleted from Redis: {}", accountNumber);
    }
}