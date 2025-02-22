package org.example.transactionprocessor.mapper;

import org.example.transactionprocessor.entity.Balance;
import org.example.transactionprocessor.entity.dto.BalanceDto;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between {@link Balance} and {@link BalanceDto}.
 * This interface provides methods to map entities to DTOs and vice versa.
 * It is used to transfer data between layers of the application, such as service and controller.
 * The mapper is annotated with {@link Mapper} to be used with MapStruct and Spring integration.
 */
@Mapper(componentModel = "spring")
public interface BalanceMapper {

    /**
     * Converts a {@link Balance} entity to a {@link BalanceDto}.
     *
     * @param balance The {@link Balance} entity to convert.
     * @return The corresponding {@link BalanceDto}.
     */
    BalanceDto toDto(Balance balance);

    /**
     * Converts a {@link BalanceDto} to a {@link Balance} entity.
     *
     * @param balanceDto The {@link BalanceDto} to convert.
     * @return The corresponding {@link Balance} entity.
     */
    Balance toEntity(BalanceDto balanceDto);
}

