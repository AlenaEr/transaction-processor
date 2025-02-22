package org.example.transactionprocessor.service;

import org.example.transactionprocessor.entity.Balance;
import org.example.transactionprocessor.exceptions.InsufficientFundsException;

import java.math.BigDecimal;

/**
 * Service interface for managing the balance of accounts.
 * Provides methods for fetching, depositing, and withdrawing amounts from an account.
 */
public interface BalanceService {

    /**
     * Retrieves the balance of an account.
     *
     * @param accountNumber The account number to fetch the balance for.
     * @return The current balance of the account as a BigDecimal.
     * @throws IllegalArgumentException if the account does not exist or the account number is invalid.
     */
    BigDecimal getBalance(String accountNumber);

    /**
     * Deposits an amount into an account.
     *
     * @param accountNumber The account number to deposit the amount into.
     * @param amount The amount to deposit. Must be positive.
     * @return The updated balance of the account after the deposit.
     * @throws IllegalArgumentException if the account does not exist or the amount is invalid.
     * @throws InsufficientFundsException if the deposit amount is negative.
     */
    Balance deposit(String accountNumber, BigDecimal amount);

    /**
     * Withdraws an amount from an account.
     *
     * @param accountNumber The account number to withdraw the amount from.
     * @param amount The amount to withdraw. Must be positive and less than or equal to the current balance.
     * @return The updated balance of the account after the withdrawal.
     * @throws IllegalArgumentException if the account does not exist, the amount is invalid, or the withdrawal exceeds the balance.
     * @throws InsufficientFundsException if the account does not have enough funds to complete the withdrawal.
     */
    Balance withdraw(String accountNumber, BigDecimal amount);
}


