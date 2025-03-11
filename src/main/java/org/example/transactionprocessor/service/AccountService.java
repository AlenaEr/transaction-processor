package org.example.transactionprocessor.service;

import jakarta.transaction.Transactional;
import org.example.transactionprocessor.entity.Account;
import org.example.transactionprocessor.entity.dto.AccountResponse;

import java.util.List;

/**
 * Service interface for managing accounts in the system.
 * Provides methods for creating, retrieving, updating, and deleting accounts.
 */
public interface AccountService {

//    /**
//     * Creates a new account with the provided details.
//     *
//     * @param account The account to be created, containing valid details.
//     * @return The created account with assigned ID and other information.
//     * @throws IllegalArgumentException if the account details are invalid or incomplete.
//     */
//    Account createAccount(Account account);

    @Transactional
    AccountResponse createAccount(AccountResponse accountResponse);

    /**
     * Retrieves an account by its account number.
     *
     * @param accountNumber The account number to search for.
     * @return The account DTO containing the account details.
     * @throws IllegalArgumentException if the account with the specified number does not exist.
     */
    AccountResponse getAccountByNumber(String accountNumber);

    /**
     * Updates the details of an existing account.
     *
     * @param account The updated account details.
     * @throws IllegalArgumentException if the account does not exist or the provided details are invalid.
     */
    void updateAccount(Account account);

    /**
     * Deletes an account by its account number.
     *
     * @param accountNumber The account number of the account to delete.
     * @throws IllegalArgumentException if the account does not exist.
     */
    void deleteAccount(String accountNumber);

    /**
     * Retrieves all accounts in the system.
     *
     * @return A list of account DTOs containing the details of all accounts.
     */
    List<AccountResponse> getAllAccounts();
}
