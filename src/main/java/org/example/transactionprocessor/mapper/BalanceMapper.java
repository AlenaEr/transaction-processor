package org.example.transactionprocessor.mapper;

import org.example.transactionprocessor.entity.Balance;
import org.example.transactionprocessor.entity.dto.BalanceDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BalanceMapper {
    BalanceDto toDto(Balance balance);

    Balance toEntity(BalanceDto balanceDto);
}
