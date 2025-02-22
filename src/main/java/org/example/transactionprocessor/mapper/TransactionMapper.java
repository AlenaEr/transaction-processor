package org.example.transactionprocessor.mapper;

import org.example.transactionprocessor.entity.Transaction;
import org.example.transactionprocessor.entity.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between {@link Transaction} and {@link TransactionDto}.
 * This interface provides methods to map transaction entities to DTOs and vice versa.
 * It is used for transferring transaction data between different layers of the application.
 * <p>
 * The mapper is annotated with {@link Mapper} to be used with MapStruct and Spring integration.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper {

    /**
     * Converts a {@link TransactionDto} to a {@link Transaction} entity.
     *
     * @param transactionDto The {@link TransactionDto} to convert.
     * @return The corresponding {@link Transaction} entity.
     */
    @Mapping(source = "accountFrom", target = "accountFrom")
    @Mapping(source = "accountTo", target = "accountTo")
    @Mapping(source = "amount", target = "amount")
    Transaction toEntity(TransactionDto transactionDto);

    /**
     * Converts a {@link Transaction} entity to a {@link TransactionDto}.
     *
     * @param transaction The {@link Transaction} entity to convert.
     * @return The corresponding {@link TransactionDto}.
     */
    @Mapping(source = "accountFrom", target = "accountFrom")
    @Mapping(source = "accountTo", target = "accountTo")
    @Mapping(source = "amount", target = "amount")
    TransactionDto toDto(Transaction transaction);
}

