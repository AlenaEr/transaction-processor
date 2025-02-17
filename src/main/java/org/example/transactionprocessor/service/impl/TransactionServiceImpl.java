package org.example.transactionprocessor.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.transactionprocessor.mapper.TransactionMapper;
import org.example.transactionprocessor.entity.Transaction;
import org.example.transactionprocessor.entity.dto.TransactionDto;
import org.example.transactionprocessor.repository.TransactionRepository;
import org.example.transactionprocessor.service.TransactionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final Executor transactionExecutor;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  @Qualifier("transactionExecutor") Executor transactionExecutor, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionExecutor = transactionExecutor;
        this.transactionMapper = transactionMapper;
    }

    public List<TransactionDto> getAllTransactions() {
        log.info("Fetching all transactions from the database");
        return transactionRepository.findAll()
                .stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    @Async("transactionExecutor")
    public CompletableFuture<TransactionDto> createTransaction(TransactionDto transactionDto) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Processing transaction: {}", transactionDto);
            Transaction entity = transactionMapper.toEntity(transactionDto);
            Transaction savedEntity = transactionRepository.save(entity);
            TransactionDto savedTransactionDto = transactionMapper.toDto(savedEntity);
            log.info("Transaction saved: {}", savedTransactionDto);
            return savedTransactionDto;
        }, transactionExecutor);
    }

    @Transactional
    public CompletableFuture<List<TransactionDto>> createTransactionsBatch(List<TransactionDto> transactionDtos) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Processing batch of {} transactions in thread {}", transactionDtos.size(), Thread.currentThread().getName());
            List<Transaction> transactions = transactionDtos.stream()
                    .map(transactionMapper::toEntity)
                    .collect(Collectors.toList());
            for (Transaction transaction : transactions) {
                if (transaction.getAccountFrom() == null || transaction.getAccountTo() == null) {
                    log.error("Invalid transaction data: {}", transaction);
                    throw new IllegalArgumentException("Invalid transaction data");
                }
            }
            List<Transaction> savedTransactions = transactionRepository.saveAll(transactions);
            log.info("Successfully processed and saved batch of {} transactions", savedTransactions.size());
            List<TransactionDto> savedTransactionDtos = savedTransactions.stream()
                    .map(transactionMapper::toDto)
                    .collect(Collectors.toList());
            return savedTransactionDtos;
        }, transactionExecutor);
    }
}
