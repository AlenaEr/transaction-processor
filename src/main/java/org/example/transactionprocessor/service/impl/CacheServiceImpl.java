package org.example.transactionprocessor.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.transactionprocessor.service.CacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class CacheServiceImpl implements CacheService {

    private final RedisTemplate<String, BigDecimal> redisTemplate;

    public CacheServiceImpl(RedisTemplate<String, BigDecimal> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveBalance(String accountNumber, BigDecimal balance) {
        redisTemplate.opsForValue().set(accountNumber, balance);
        log.info("Balance saved in Redis: {} -> {}", accountNumber, balance);
    }

    public BigDecimal getBalance(String accountNumber) {
        BigDecimal balance = redisTemplate.opsForValue().get(accountNumber);
        log.info("Getting balance from Redis: {} -> {}", accountNumber, balance);
        return balance;
    }

    public void deleteBalance(String accountNumber) {
        redisTemplate.delete(accountNumber);
        log.info("Balance deleted from Redis: {}", accountNumber);
    }
}
