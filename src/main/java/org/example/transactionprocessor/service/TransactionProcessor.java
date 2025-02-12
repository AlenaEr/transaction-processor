package org.example.transactionprocessor.service;

import org.example.transactionprocessor.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class TransactionProcessor {
    private static final Logger log = LoggerFactory.getLogger(TransactionProcessor.class);
    private final TransactionService transactionService;
    private final BlockingQueue<Transaction> transactionQueue = new LinkedBlockingQueue<>();
    private final Executor executor;

    @Autowired
    public TransactionProcessor(TransactionService transactionService, @Qualifier("taskExecutor") Executor executor) {
        this.transactionService = transactionService;
        this.executor = executor;
        startProcessing();
    }

    public Transaction processTransaction(Transaction transaction) {
        transactionQueue.offer(transaction);
        return transaction;
    }

    private void startProcessing() {
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                while (true) {
                    try {
                        Transaction transaction = transactionQueue.take();
                        transactionService.createTransaction(transaction);
                        log.info("Processed transaction: {}", transaction);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.error("Transaction processing interrupted", e);
                        break;
                    }
                }
            });
        }
    }

    public List<Transaction> processTransactionsBatch(List<Transaction> transactions) {
        transactions.forEach(this::processTransaction);
        return transactions;
    }
}
