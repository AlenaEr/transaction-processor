package org.example.transactionprocessor.service;

import jakarta.transaction.Transactional;
import org.example.transactionprocessor.model.Transaction;
import org.example.transactionprocessor.repository.TransactionRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionProcessor transactionProcessor;

    public TransactionService(TransactionRepository transactionRepository, TransactionProcessor transactionProcessor) {
        this.transactionRepository = transactionRepository;
        this.transactionProcessor = transactionProcessor;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    //TODO Add validation,additional logic..
    @Async
    public CompletableFuture<Transaction> createTransaction(Transaction transaction) {
        Transaction saveTransaction = transactionProcessor.processTransaction(transaction);
        return CompletableFuture.completedFuture(saveTransaction);
    }

    @Transactional
    public List<Transaction> createTransactionsBatch(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (transaction.getAccountFrom() == null || transaction.getAccountTo() == null) {
                throw new IllegalArgumentException("Invalid transaction data");
            }
        }
        List<Transaction> savedTransactions = transactionProcessor.processTransactionsBatch(transactions);
        return transactionRepository.saveAll(savedTransactions);
    }
}
