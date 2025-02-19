package org.example.transactionprocessor.service;

import org.example.transactionprocessor.entity.Account;
import org.example.transactionprocessor.entity.dto.AccountDto;

import java.util.List;

public interface AccountService {

    Account createAccount(Account account);

    AccountDto getAccountByNumber(String accountNumber);

    void updateAccount(Account account);

    void deleteAccount(String accountNumber);

    List<AccountDto> getAllAccounts();
}
