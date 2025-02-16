package org.example.transactionprocessor.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(
        String accountFrom,
        String accountTo,
        BigDecimal amount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
