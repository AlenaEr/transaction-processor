package org.example.transactionprocessor.service;

import org.example.transactionprocessor.entity.Balance;

import java.math.BigDecimal;

public interface CacheService {

    void saveBalance(String accountNumber, BigDecimal balance);

    BigDecimal getBalance(String accountNumber);

    void deleteBalance(String accountNumber);
}
