package org.example.transactionprocessor.controller;

import org.example.transactionprocessor.entity.Balance;
import org.example.transactionprocessor.service.BalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/balances")
public class BalanceController {

    BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<String> deposit(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
        try {
            Balance balance = balanceService.deposit(accountNumber, amount);
            return ResponseEntity.ok("Deposit successful. New balance: " + balance.getAmount());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error during deposit: " + e.getMessage());
        }
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<String> withdraw(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
        try {
            Balance balance = balanceService.withdraw(accountNumber, amount);
            return ResponseEntity.ok("Withdrawal successful. New balance: " + balance.getAmount());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error during withdrawal: " + e.getMessage());
        }
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String accountNumber) {
        BigDecimal balance = balanceService.getBalance(accountNumber);
        return ResponseEntity.ok(balance);
    }
}
