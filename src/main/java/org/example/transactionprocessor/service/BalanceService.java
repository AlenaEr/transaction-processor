package org.example.transactionprocessor.service;

import org.example.transactionprocessor.entity.Balance;

import java.math.BigDecimal;

public interface BalanceService {
    BigDecimal getBalance(String accountNumber);

    Balance deposit(String accountNumber, BigDecimal amount);

    Balance withdraw(String accountNumber, BigDecimal amount);
}
