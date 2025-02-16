package org.example.transactionprocessor.entity.dto;

public record AccountDto(Long id, String accountNumber, BalanceDto balance) {
}
