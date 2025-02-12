package org.example.transactionprocessor.controller;

import org.example.transactionprocessor.model.Transaction;
import org.example.transactionprocessor.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
@Controller + @ResponseBody. It is used for building REST APIs and always returns data in JSON or XML format, rather than HTML pages.
*/
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<CompletableFuture<Transaction>> createTransaction(@RequestBody Transaction transaction) {
        CompletableFuture<Transaction> createdTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.ok(createdTransaction);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Transaction>> createTransactionsBatch(@RequestBody List<Transaction> transactions) {
        List<Transaction> createdTransactions = transactionService.createTransactionsBatch(transactions);
        return ResponseEntity.ok(createdTransactions);
    }
}
