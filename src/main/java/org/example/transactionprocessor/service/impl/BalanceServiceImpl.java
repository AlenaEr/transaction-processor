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

/**
 * Implementation of the {@link BalanceService} interface that handles balance retrieval, deposit, and withdrawal operations.
 * This service interacts with the {@link AccountRepository} to retrieve account data, the {@link BalanceRepository}
 * for saving balance data, and {@link CacheService} to cache balance information for faster retrieval.
 * <p>
 * The service ensures that the balance is retrieved from the cache if available, otherwise, it fetches it from the database
 * and caches it for future use. It also provides methods to deposit and withdraw funds, applying appropriate balance checks
 * (e.g., ensuring there are sufficient funds before withdrawal).
 * </p>
 *
 * @see BalanceService
 * @see AccountRepository
 * @see BalanceRepository
 * @see CacheService
 */
@Slf4j
@Service
public class BalanceServiceImpl implements BalanceService {

    private final AccountRepository accountRepository;
    private final BalanceRepository balanceRepository;
    private final CacheService cacheService;

    /**
     * Constructor for BalanceServiceImpl.
     * Initializes the necessary repositories and cache service.
     *
     * @param accountRepository The repository for managing account data.
     * @param balanceRepository The repository for managing balance data.
     * @param cacheService The service for caching balance data.
     */
    public BalanceServiceImpl(AccountRepository accountRepository, BalanceRepository balanceRepository, CacheService cacheService) {
        this.accountRepository = accountRepository;
        this.balanceRepository = balanceRepository;
        this.cacheService = cacheService;
    }

    /**
     * Retrieves the balance for the given account. If the balance is null,
     * a new balance with zero amount is created.
     *
     * @param account The account for which the balance is retrieved or created.
     * @return The balance of the account.
     */
    private Balance getOrCreateBalance(Account account) {
        Balance balance = account.getBalance();
        return (balance != null) ? balance : new Balance(BigDecimal.ZERO);
    }

    /**
     * Fetches an account based on the provided account number.
     *
     * @param accountNumber The account number to look up.
     * @return The Account object.
     * @throws AccountNotFoundException if the account is not found.
     */
    private Account getAccountByNumber(String accountNumber) {
        try {
            return accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        } catch (AccountNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the current balance of the specified account.
     * If the balance is cached, it is returned from the cache.
     *
     * @param accountNumber The account number for which the balance is requested.
     * @return The balance of the account as a BigDecimal.
     * @throws IllegalArgumentException if the account does not exist.
     */
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

    /**
     * Deposits a specified amount into the account.
     * The new balance is saved both in the database and cache.
     *
     * @param accountNumber The account number into which the deposit is made.
     * @param amount The amount to deposit. Must be positive.
     * @return The updated balance of the account.
     * @throws IllegalArgumentException if the account does not exist or the amount is invalid.
     */
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

    /**
     * Withdraws a specified amount from the account.
     * The withdrawal is allowed only if there are sufficient funds.
     * The new balance is saved both in the database and cache.
     *
     * @param accountNumber The account number to withdraw the amount from.
     * @param amount The amount to withdraw. Must be positive and less than or equal to the current balance.
     * @return The updated balance after the withdrawal.
     * @throws IllegalArgumentException if the account does not exist or the withdrawal exceeds the available balance.
     * @throws InsufficientFundsException if there are insufficient funds for the withdrawal.
     */
    @Override
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


