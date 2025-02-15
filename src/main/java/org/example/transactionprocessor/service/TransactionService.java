package org.example.transactionprocessor.service;

import org.example.transactionprocessor.model.Transaction;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TransactionService {
    List<Transaction> getAllTransactions();
    CompletableFuture<Transaction> createTransaction(Transaction transaction);
    CompletableFuture<List<Transaction>> createTransactionsBatch(List<Transaction> transactions);
}
