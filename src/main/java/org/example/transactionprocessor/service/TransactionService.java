package org.example.transactionprocessor.service;

import org.example.transactionprocessor.model.Transaction;
import org.example.transactionprocessor.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TransactionService {
    @Autowired
    private final TransactionRepository transactionRepository;
    private final ReentrantLock lock = new ReentrantLock();

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    //TODO Add validation,additional logic..
    @Async
    public CompletableFuture<Transaction> createTransaction(Transaction transaction) {
        lock.lock();
        try {
            Transaction saveTransaction = transactionRepository.save(transaction);
            return CompletableFuture.completedFuture(saveTransaction);
        } finally {
            lock.unlock();
        }
    }

    public List<Transaction> createTransactionsBatch(List<Transaction> transactions) {
        return transactionRepository.saveAll(transactions);
    }
}
