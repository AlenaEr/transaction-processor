package org.example.transactionprocessor.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.transactionprocessor.entity.Account;
import org.example.transactionprocessor.entity.dto.AccountDto;
import org.example.transactionprocessor.mapper.AccountMapper;
import org.example.transactionprocessor.repository.AccountRepository;
import org.example.transactionprocessor.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public AccountDto getAccountByNumber(String accountNumber) {
        log.info("Fetching account with accountNumber: {}", accountNumber);
        Optional<Account> accountOptional = accountRepository.findByAccountNumber(accountNumber);
        if (accountOptional.isPresent()) {
            return accountMapper.toDto(accountOptional.get());
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
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));

        accountRepository.delete(account);
        log.info("Deleted account: {}", accountNumber);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(accountMapper::toDto)
                .collect(Collectors.toList());
    }
}
