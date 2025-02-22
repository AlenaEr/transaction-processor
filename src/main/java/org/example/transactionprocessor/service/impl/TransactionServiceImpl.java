package org.example.transactionprocessor.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.transactionprocessor.entity.Account;
import org.example.transactionprocessor.entity.Transaction;
import org.example.transactionprocessor.entity.dto.TransactionDto;
import org.example.transactionprocessor.exceptions.InsufficientFundsException;
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

/**
 * Implementation of {@link TransactionService} that handles financial transactions.
 * <p>
 * This service is responsible for processing single and batch transactions,
 * ensuring balance updates, and maintaining transaction records.
 */
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final BalanceService balanceService;
    private final TransactionMapper transactionMapper;
    private final BalanceMapper balanceMapper;
    private final AccountMapper accountMapper;

    /**
     * Constructor to initialize the dependencies.
     *
     * @param transactionRepository Repository for transaction-related operations.
     * @param accountService Service for account-related operations.
     * @param balanceService Service for balance-related operations.
     * @param transactionMapper Mapper for converting Transaction entities to DTOs.
     * @param balanceMapper Mapper for converting Balance entities to DTOs.
     * @param accountMapper Mapper for converting Account entities to DTOs.
     */
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

    /**
     * Retrieves all transactions from the repository.
     *
     * @return A list of all transactions as TransactionDto objects.
     */
    @Override
    public List<TransactionDto> getAllTransactions() {
        log.info("Fetching all transactions from the database");
        return transactionRepository.findAll()
                .stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Creates a single transaction asynchronously.
     * Processes the transaction, validates funds, and saves it to the repository.
     *
     * @param transactionDto The transaction to be created as TransactionDto.
     * @return A CompletableFuture with the created transaction as TransactionDto.
     * @throws InsufficientFundsException if there are not enough funds for the transaction.
     */
    @Transactional
    @Override
    public CompletableFuture<TransactionDto> createTransaction(TransactionDto transactionDto) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Processing transaction: {}", transactionDto);

            Account accountFrom = accountMapper.toEntity(accountService.getAccountByNumber(transactionDto.accountFrom()));
            Account accountTo = accountMapper.toEntity(accountService.getAccountByNumber(transactionDto.accountTo()));

            BigDecimal balanceFrom = balanceService.getBalance(accountFrom.getAccountNumber());

            if (balanceFrom.compareTo(transactionDto.amount()) < 0) {
                throw new InsufficientFundsException("Insufficient funds on account " + accountFrom.getAccountNumber());
            }

            balanceService.withdraw(accountFrom.getAccountNumber(), transactionDto.amount());
            balanceService.deposit(accountTo.getAccountNumber(), transactionDto.amount());

            Transaction transaction = transactionMapper.toEntity(transactionDto);
            Transaction savedTransaction = transactionRepository.save(transaction);
            log.info("Transaction saved: {}", savedTransaction);

            return transactionMapper.toDto(savedTransaction);
        });
    }

    /**
     * Creates a batch of transactions asynchronously.
     * Processes and validates each transaction in the batch, and saves them to the repository.
     *
     * @param transactionDtos The list of transactions to be created as TransactionDto objects.
     * @return A CompletableFuture with a list of created transactions as TransactionDto objects.
     * @throws IllegalArgumentException if any transaction is invalid or if there are insufficient funds.
     */
    @Transactional
    @Override
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

