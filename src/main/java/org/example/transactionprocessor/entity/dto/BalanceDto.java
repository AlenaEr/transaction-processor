package org.example.transactionprocessor.entity.dto;

import java.math.BigDecimal;

public record BalanceDto(Long id, BigDecimal amount) {
}
