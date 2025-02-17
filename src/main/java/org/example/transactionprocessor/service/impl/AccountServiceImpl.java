package org.example.transactionprocessor.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.transactionprocessor.entity.Account;
import org.example.transactionprocessor.entity.Balance;
import org.example.transactionprocessor.entity.dto.AccountDto;
import org.example.transactionprocessor.entity.dto.BalanceDto;
import org.example.transactionprocessor.mapper.AccountMapper;
import org.example.transactionprocessor.repository.AccountRepository;
import org.example.transactionprocessor.service.AccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public Account createAccount(Account account) {
        log.info("Creating new account: {}", account);
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {
        log.info("Fetching account with accountNumber: {}", accountNumber);
        Optional<Account> accountOptional = accountRepository.findByAccountNumber(accountNumber);
        if (accountOptional.isPresent()) {
            return accountOptional.get();
        } else {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
    }

    @Override
    public void updateAccount(Account account) {
        accountRepository.save(account);
        log.info("Updated account: {}", account.getAccountNumber());
    }

    @Override
    public void deleteAccount(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        accountRepository.delete(account);
        log.info("Deleted account: {}", accountNumber);
    }
}
