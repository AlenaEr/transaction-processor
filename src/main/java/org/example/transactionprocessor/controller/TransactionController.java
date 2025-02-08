package org.example.transactionprocessor.controller;

import org.example.transactionprocessor.model.Transaction;
import org.example.transactionprocessor.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
@Controller + @ResponseBody. Она используется для построения REST API и
всегда возвращает данные в формате JSON или XML, а не HTML-страницы.
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Transaction>> createTransactionsBatch(@RequestBody List<Transaction> transactions) {
        List<Transaction> createdTransactions = transactionService.createTransactionsBatch(transactions);
        return ResponseEntity.ok(createdTransactions);
    }
}
