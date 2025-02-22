package org.example.transactionprocessor.service;

import org.example.transactionprocessor.entity.dto.TransactionDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service interface for managing transactions. Provides methods for retrieving,
 * creating individual transactions, and processing batches of transactions.
 */
public interface TransactionService {

    /**
     * Retrieves all transactions.
     *
     * @return A list of all transactions as TransactionDto objects.
     */
    List<TransactionDto> getAllTransactions();

    /**
     * Creates a new transaction asynchronously.
     *
     * @param transaction The transaction to be created as a TransactionDto.
     * @return A CompletableFuture with the created transaction as a TransactionDto.
     */
    CompletableFuture<TransactionDto> createTransaction(TransactionDto transaction);

    /**
     * Creates a batch of transactions asynchronously.
     *
     * @param transactions A list of transactions to be created as TransactionDto objects.
     * @return A CompletableFuture with a list of created transactions as TransactionDto objects.
     */
    CompletableFuture<List<TransactionDto>> createTransactionsBatch(List<TransactionDto> transactions);
}
