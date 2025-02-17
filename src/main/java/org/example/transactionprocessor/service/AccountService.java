package org.example.transactionprocessor.service;

import org.example.transactionprocessor.entity.Account;

public interface AccountService {
    Account createAccount(Account account);
    Account getAccountByNumber(String accountNumber);
    void updateAccount(Account account);
    void deleteAccount(String accountNumber);
}
