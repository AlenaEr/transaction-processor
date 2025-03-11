package org.example.transactionprocessor.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.transactionprocessor.entity.Account;
import org.example.transactionprocessor.entity.Balance;
import org.example.transactionprocessor.entity.dto.AccountResponse;
import org.example.transactionprocessor.mapper.AccountMapper;
import org.example.transactionprocessor.repository.AccountRepository;
import org.example.transactionprocessor.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of {@link AccountService}.
 * Handles the logic for account creation, retrieval, update, and deletion.
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    /**
     * Constructs the AccountService with dependencies.
     *
     * @param accountRepository The repository for account data operations.
     * @param accountMapper The mapper for converting between Account entities and DTOs.
     */
    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Transactional
    @Override
    public AccountResponse createAccount(AccountResponse accountResponse) {
        log.info("Creating account for ownerId: {}", accountResponse.ownerId());

        Account savedAccount = new Account();
        savedAccount.setOwnerId(accountResponse.ownerId());
        savedAccount.setAccountNumber(generateAccountNumber());
        savedAccount.setBalance(new Balance());

        log.info("Account {} for user {} created", savedAccount.getId(), savedAccount.getOwnerId());
        return accountMapper.toDto(savedAccount);
    }

    @Override
    public AccountResponse getAccountByNumber(String accountNumber) {
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
    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(accountMapper::toDto)
                .collect(Collectors.toList());
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString();
    }
}
