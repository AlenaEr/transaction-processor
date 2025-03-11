package org.example.transactionprocessor.entity.dto;

public record AccountResponse(Long id, Long ownerId, String accountNumber, BalanceDto balance) {
}
