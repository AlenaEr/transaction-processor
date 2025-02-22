package org.example.transactionprocessor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.transactionprocessor.entity.Account;
import org.example.transactionprocessor.entity.dto.AccountDto;
import org.example.transactionprocessor.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Controller", description = "Manages user accounts")
public class AccountController {
    private final AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Get account by number", description = "Retrieves account details by its number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable String accountNumber) {
        AccountDto accountDto = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(accountDto);
    }

    //TODO refactor, use DTO
    @Operation(summary = "Create a new account", description = "Creates a new account and returns its details")
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    //TODO refactor
    @PutMapping("/{accountNumber}/update")
    public ResponseEntity<Account> updateAccount(@PathVariable String accountNumber, @RequestBody Account account) {
        account.setAccountNumber(accountNumber);
        accountService.updateAccount(account);
        return ResponseEntity.ok(account);
    }

    /**
     * Deletes an account by account number.
     *
     * @param accountNumber the account number of the account to be deleted
     * @return a response indicating the success of the deletion
     */
    @Operation(summary = "Delete account", description = "Deletes an account using the account number.")
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<String> deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.ok("Account deleted successfully");
    }

    /**
     * Retrieves all accounts.
     *
     * @return a list of all accounts
     */
    @Operation(summary = "Get all accounts", description = "Retrieves a list of all accounts.")
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<AccountDto> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
}
