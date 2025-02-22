package org.example.transactionprocessor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.transactionprocessor.entity.dto.TransactionDto;
import org.example.transactionprocessor.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for managing transactions.
 */
@Tag(name = "Transaction Controller", description = "Endpoints for managing financial transactions")
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Retrieves all transactions.
     *
     * @return a list of all transactions
     */
    @Operation(summary = "Get all transactions", description = "Retrieves a list of all transactions.")
    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    /**
     * Creates a new transaction (funds transfer).
     *
     * @param transaction the transaction details
     * @return the created transaction
     */
    @Operation(summary = "Create a transaction", description = "Transfers funds from one account to another.")
    @PostMapping("/transfer")
    public CompletableFuture<ResponseEntity<TransactionDto>> createTransaction(
            @Parameter(description = "Transaction details") @RequestBody TransactionDto transaction) {
        return transactionService.createTransaction(transaction)
                .thenApply(ResponseEntity::ok);
    }

    /**
     * Creates multiple transactions in batch mode.
     *
     * @param transactions the list of transactions
     * @return the created transactions
     */
    @Operation(summary = "Create multiple transactions", description = "Transfers funds for multiple transactions in batch mode.")
    @PostMapping("/transfer/batch")
    public CompletableFuture<ResponseEntity<List<TransactionDto>>> createTransactionsBatch(
            @Parameter(description = "List of transactions to process") @RequestBody List<TransactionDto> transactions) {
        return transactionService.createTransactionsBatch(transactions)
                .thenApply(ResponseEntity::ok);
    }
}

