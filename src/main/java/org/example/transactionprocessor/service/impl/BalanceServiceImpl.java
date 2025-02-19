package org.example.transactionprocessor.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.transactionprocessor.entity.Account;
import org.example.transactionprocessor.entity.Balance;
import org.example.transactionprocessor.exceptions.InsufficientFundsException;
import org.example.transactionprocessor.repository.AccountRepository;
import org.example.transactionprocessor.repository.BalanceRepository;
import org.example.transactionprocessor.service.BalanceService;
import org.example.transactionprocessor.service.CacheService;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;

@Slf4j
@Service
public class BalanceServiceImpl implements BalanceService {

    private final AccountRepository accountRepository;
    private final BalanceRepository balanceRepository;
    private final CacheService cacheService;

    public BalanceServiceImpl(AccountRepository accountRepository, BalanceRepository balanceRepository, CacheService cacheService) {
        this.accountRepository = accountRepository;
        this.balanceRepository = balanceRepository;
        this.cacheService = cacheService;
    }

    private Balance getOrCreateBalance(Account account) {
        Balance balance = account.getBalance();
        return (balance != null) ? balance : new Balance(BigDecimal.ZERO);
    }

    private Account getAccountByNumber(String accountNumber) {
        try {
            return accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        } catch (AccountNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BigDecimal getBalance(String accountNumber) {
        BigDecimal cachedBalance = cacheService.getBalance(accountNumber);
        if (cachedBalance != null) {
            return cachedBalance;
        }

        Account account = getAccountByNumber(accountNumber);
        Balance balance = getOrCreateBalance(account);
        cacheService.saveBalance(accountNumber, balance.getAmount());

        return balance.getAmount();
    }

    //TODO add source type(ATM, online...)
    @Override
    public Balance deposit(String accountNumber, BigDecimal amount) {
        Account account = getAccountByNumber(accountNumber);
        Balance balance = getOrCreateBalance(account);

        BigDecimal currentAmount = balance.getAmount();
        BigDecimal updatedAmount = currentAmount.add(amount);
        balance.setAmount(updatedAmount);
        balance.setAccount(account);
        balanceRepository.save(balance);
        cacheService.saveBalance(accountNumber, updatedAmount);

        return balance;
    }

    @Override//TODO add source type(ATM, online...)
    public Balance withdraw(String accountNumber, BigDecimal amount) {
        Account account = getAccountByNumber(accountNumber);
        Balance balance = getOrCreateBalance(account);

        if (balance.getAmount().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        BigDecimal currentAmount = balance.getAmount();
        BigDecimal updatedAmount = currentAmount.subtract(amount);
        balance.setAmount(updatedAmount);
        balance.setAccount(account);
        balanceRepository.save(balance);
        cacheService.saveBalance(accountNumber, updatedAmount);

        return balance;
    }
}

