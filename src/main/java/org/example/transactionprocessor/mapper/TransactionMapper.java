package org.example.transactionprocessor.mapper;

import org.example.transactionprocessor.entity.Transaction;
import org.example.transactionprocessor.entity.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source = "accountFrom", target = "accountFrom")
    @Mapping(source = "accountTo", target = "accountTo")
    @Mapping(source = "amount", target = "amount")
    Transaction toEntity(TransactionDto transactionDto);

    @Mapping(source = "accountFrom", target = "accountFrom")
    @Mapping(source = "accountTo", target = "accountTo")
    @Mapping(source = "amount", target = "amount")
    TransactionDto toDto(Transaction transaction);
}
