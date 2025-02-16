package org.example.transactionprocessor.mapper;

import org.example.transactionprocessor.entity.Transaction;
import org.example.transactionprocessor.entity.dto.TransactionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionDto toDto(Transaction entity);

    Transaction toEntity(TransactionDto dto);
}
