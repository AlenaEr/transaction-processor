package org.example.transactionprocessor.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.transactionprocessor.entity.Account;
import org.example.transactionprocessor.entity.Transaction;
import org.example.transactionprocessor.entity.dto.TransactionDto;
import org.example.transactionprocessor.mapper.AccountMapper;
import org.example.transactionprocessor.mapper.BalanceMapper;
import org.example.transactionprocessor.mapper.TransactionMapper;
import org.example.transactionprocessor.repository.TransactionRepository;
import org.example.transactionprocessor.service.AccountService;
import org.example.transactionprocessor.service.BalanceService;
import org.example.transactionprocessor.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final BalanceService balanceService;
    private final TransactionMapper transactionMapper;
    private final BalanceMapper balanceMapper;
    private final AccountMapper accountMapper;


    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountService accountService,
                                  BalanceService balanceService,
                                  TransactionMapper transactionMapper, BalanceMapper balanceMapper, AccountMapper accountMapper) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.balanceService = balanceService;
        this.transactionMapper = transactionMapper;
        this.balanceMapper = balanceMapper;
        this.accountMapper = accountMapper;
    }

    @Override
    public List<TransactionDto> getAllTransactions() {
        log.info("Fetching all transactions from the database");
        return transactionRepository.findAll()
                .stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CompletableFuture<TransactionDto> createTransaction(TransactionDto transactionDto) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Processing transaction: {}", transactionDto);

            Account accountFrom = accountMapper.toEntity(accountService.getAccountByNumber(transactionDto.accountFrom()));
            Account accountTo = accountMapper.toEntity(accountService.getAccountByNumber(transactionDto.accountTo()));

            BigDecimal balanceFrom = balanceService.getBalance(accountFrom.getAccountNumber());

            if (balanceFrom.compareTo(transactionDto.amount()) < 0) {
                throw new IllegalArgumentException("Insufficient funds on account " + accountFrom.getAccountNumber());
            }

            balanceService.withdraw(accountFrom.getAccountNumber(), transactionDto.amount());
            balanceService.deposit(accountTo.getAccountNumber(), transactionDto.amount());

            Transaction transaction = transactionMapper.toEntity(transactionDto);
            Transaction savedTransaction = transactionRepository.save(transaction);
            log.info("Transaction saved: {}", savedTransaction);

            return transactionMapper.toDto(savedTransaction);
        });
    }

    @Transactional
    public CompletableFuture<List<TransactionDto>> createTransactionsBatch(List<TransactionDto> transactionDtos) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Processing batch of {} transactions in thread {}", transactionDtos.size(), Thread.currentThread().getName());

            List<TransactionDto> processedTransactions = new ArrayList<>();

            for (TransactionDto transactionDto : transactionDtos) {

                Account accountFrom = accountMapper.toEntity(accountService.getAccountByNumber(transactionDto.accountFrom()));
                Account accountTo = accountMapper.toEntity(accountService.getAccountByNumber(transactionDto.accountTo()));

                if (accountFrom == null || accountTo == null) {
                    log.error("Invalid transaction: accountFrom or accountTo is null: {}", transactionDto);
                    throw new IllegalArgumentException("Invalid transaction: account not found");
                }

                BigDecimal balanceFrom = balanceService.getBalance(accountFrom.getAccountNumber());

                if (balanceFrom.compareTo(transactionDto.amount()) < 0) {
                    log.error("Insufficient funds on account {}", accountFrom.getAccountNumber());
                    throw new IllegalArgumentException("Insufficient funds on account " + accountFrom.getAccountNumber());
                }

                balanceService.withdraw(accountFrom.getAccountNumber(), transactionDto.amount());
                balanceService.deposit(accountTo.getAccountNumber(), transactionDto.amount());

                Transaction transaction = transactionMapper.toEntity(transactionDto);
                transaction.setAccountFrom(accountFrom.getAccountNumber());
                transaction.setAccountTo(accountTo.getAccountNumber());
                Transaction savedTransaction = transactionRepository.save(transaction);

                processedTransactions.add(transactionMapper.toDto(savedTransaction));
            }

            log.info("Successfully processed and saved batch of {} transactions", processedTransactions.size());
            return processedTransactions;
        });
    }
}
