package org.example.transactionprocessor.controller;

import org.example.transactionprocessor.entity.dto.TransactionDto;
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
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<TransactionDto>> createTransaction(@RequestBody TransactionDto transaction) {
        return transactionService.createTransaction(transaction)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/batch")
    public CompletableFuture<ResponseEntity<List<TransactionDto>>> createTransactionsBatch(@RequestBody List<TransactionDto> transactions) {
        return transactionService.createTransactionsBatch(transactions)
                .thenApply(ResponseEntity::ok);
    }
}

