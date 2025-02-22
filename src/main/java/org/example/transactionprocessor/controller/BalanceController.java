package org.example.transactionprocessor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.transactionprocessor.entity.Balance;
import org.example.transactionprocessor.service.BalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Controller for managing account balances.
 */
@Tag(name = "Balance Controller", description = "Endpoints for managing account balances")
@RestController
@RequestMapping("/api/balances")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    /**
     * Deposits a specified amount into an account.
     *
     * @param accountNumber the account number
     * @param amount        the amount to deposit
     * @return a message indicating the deposit result
     */
    @Operation(summary = "Deposit funds", description = "Deposits a specified amount into the given account.")
    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<String> deposit(
            @Parameter(description = "Account number to deposit into") @PathVariable String accountNumber,
            @Parameter(description = "Amount to deposit") @RequestParam BigDecimal amount) {
        try {
            Balance balance = balanceService.deposit(accountNumber, amount);
            return ResponseEntity.ok("Deposit successful. New balance: " + balance.getAmount());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error during deposit: " + e.getMessage());
        }
    }

    /**
     * Withdraws a specified amount from an account.
     *
     * @param accountNumber the account number
     * @param amount        the amount to withdraw
     * @return a message indicating the withdrawal result
     */
    @Operation(summary = "Withdraw funds", description = "Withdraws a specified amount from the given account.")
    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<String> withdraw(
            @Parameter(description = "Account number to withdraw from") @PathVariable String accountNumber,
            @Parameter(description = "Amount to withdraw") @RequestParam BigDecimal amount) {
        try {
            Balance balance = balanceService.withdraw(accountNumber, amount);
            return ResponseEntity.ok("Withdrawal successful. New balance: " + balance.getAmount());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error during withdrawal: " + e.getMessage());
        }
    }

    /**
     * Retrieves the balance for a specific account.
     *
     * @param accountNumber the account number
     * @return the balance of the account
     */
    @Operation(summary = "Get account balance", description = "Retrieves the current balance of the given account.")
    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<BigDecimal> getBalance(
            @Parameter(description = "Account number to retrieve the balance for") @PathVariable String accountNumber) {
        BigDecimal balance = balanceService.getBalance(accountNumber);
        return ResponseEntity.ok(balance);
    }
}
