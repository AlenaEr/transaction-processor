package org.example.transactionprocessor.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.transactionprocessor.model.Transaction;
import org.example.transactionprocessor.repository.TransactionRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionProcessor transactionProcessor;

    public TransactionService(TransactionRepository transactionRepository, TransactionProcessor transactionProcessor) {
        this.transactionRepository = transactionRepository;
        this.transactionProcessor = transactionProcessor;
    }

    public List<Transaction> getAllTransactions() {
        log.info("Fetching all transactions from the database");
        List<Transaction> transactions = transactionRepository.findAll();
        log.info("Retrieved {} transactions", transactions.size());
        return transactions;
    }

    //TODO Add validation,additional logic..
    @Async
    public CompletableFuture<Transaction> createTransaction(Transaction transaction) {
        log.info("Received request to create transaction: {}", transaction);
        Transaction savedTransaction = transactionProcessor.processTransaction(transaction);
        log.info("Transaction processed and saved: {}", savedTransaction);
        return CompletableFuture.completedFuture(savedTransaction);
    }

    @Transactional
    public List<Transaction> createTransactionsBatch(List<Transaction> transactions) {
        log.info("Processing batch of {} transactions", transactions.size());
        for (Transaction transaction : transactions) {
            if (transaction.getAccountFrom() == null || transaction.getAccountTo() == null) {
                log.error("Invalid transaction data: {}", transaction);
                throw new IllegalArgumentException("Invalid transaction data");
            }
        }
        List<Transaction> processedTransactions = transactionProcessor.processTransactionsBatch(transactions);
        List<Transaction> savedTransactions = transactionRepository.saveAll(processedTransactions);
        log.info("Successfully processed and saved batch of {} transactions", savedTransactions.size());
        return savedTransactions;
    }
}
