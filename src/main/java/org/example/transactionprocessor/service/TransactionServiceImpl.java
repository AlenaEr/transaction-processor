package org.example.transactionprocessor.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.transactionprocessor.model.Transaction;
import org.example.transactionprocessor.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final Executor transactionExecutor;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  @Qualifier("transactionExecutor") Executor transactionExecutor) {
        this.transactionRepository = transactionRepository;
        this.transactionExecutor = transactionExecutor;
    }

    public List<Transaction> getAllTransactions() {
        log.info("Fetching all transactions from the database");
        List<Transaction> transactions = transactionRepository.findAll();
        log.info("Retrieved {} transactions", transactions.size());
        return transactions;
    }

    @Async("transactionExecutor")
    public CompletableFuture<Transaction> createTransaction(Transaction transaction) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Processing transaction: {}", transaction);
            Transaction savedTransaction = transactionRepository.save(transaction);
            log.info("Transaction saved: {}", savedTransaction);
            return savedTransaction;
        }, transactionExecutor);
    }

    @Transactional
    public CompletableFuture<List<Transaction>> createTransactionsBatch(List<Transaction> transactions) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Processing batch of {} transactions in thread {}", transactions.size(), Thread.currentThread().getName());
            for (Transaction transaction : transactions) {
                if (transaction.getAccountFrom() == null || transaction.getAccountTo() == null) {
                    log.error("Invalid transaction data: {}", transaction);
                    throw new IllegalArgumentException("Invalid transaction data");
                }
            }
            List<Transaction> savedTransactions = transactionRepository.saveAll(transactions);
            log.info("Successfully processed and saved batch of {} transactions", savedTransactions.size());
            return savedTransactions;
        }, transactionExecutor);
    }
}
