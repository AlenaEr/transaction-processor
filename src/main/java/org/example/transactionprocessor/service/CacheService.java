package org.example.transactionprocessor.service;

import java.math.BigDecimal;

/**
 * Service interface for managing account balances in cache.
 * Provides methods for saving, retrieving, and deleting balances.
 */
public interface CacheService {

    /**
     * Saves the balance of an account in the cache.
     *
     * @param accountNumber The account number for which the balance is being saved.
     * @param balance The balance to be saved.
     */
    void saveBalance(String accountNumber, BigDecimal balance);

    /**
     * Retrieves the balance of an account from the cache.
     *
     * @param accountNumber The account number for which the balance is requested.
     * @return The balance of the account, or null if not found in the cache.
     */
    BigDecimal getBalance(String accountNumber);

    /**
     * Deletes the balance of an account from the cache.
     *
     * @param accountNumber The account number for which the balance is being deleted.
     */
    void deleteBalance(String accountNumber);
}

