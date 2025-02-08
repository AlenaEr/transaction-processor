package org.example.transactionprocessor.utils;

import jakarta.annotation.PostConstruct;
import org.example.transactionprocessor.model.Transaction;
import org.example.transactionprocessor.repository.TransactionRepository;
import org.example.transactionprocessor.service.TransactionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader {
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    public DataLoader(TransactionService transactionService, TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    @PostConstruct
    public void init() {
        // Если таблица пуста, добавляем транзакции
        List<Transaction> transactions = List.of(
                new Transaction("account1", "account2", 1000000.0),
                new Transaction("account2", "account3", 200.0),
                new Transaction("account1", "account3", 300.0),
                new Transaction("account4", "account1", 400.0)
        );
        transactionService.createTransactionsBatch(transactions);
    }
}
