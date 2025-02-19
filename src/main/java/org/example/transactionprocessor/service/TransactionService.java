package org.example.transactionprocessor.service;

import org.example.transactionprocessor.entity.dto.TransactionDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TransactionService {
    List<TransactionDto> getAllTransactions();

    CompletableFuture<TransactionDto> createTransaction(TransactionDto transaction);

    CompletableFuture<List<TransactionDto>> createTransactionsBatch(List<TransactionDto> transactions);
}
