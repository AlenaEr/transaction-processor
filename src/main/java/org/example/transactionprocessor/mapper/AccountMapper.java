package org.example.transactionprocessor.mapper;

import org.example.transactionprocessor.entity.Account;
import org.example.transactionprocessor.entity.dto.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between {@link Account} and {@link AccountResponse} objects.
 * Utilizes {@link BalanceMapper} for mapping the balance field.
 * @see BalanceMapper
 * The mapper is annotated with {@link Mapper} to be used with MapStruct and Spring integration.
 */
@Mapper(componentModel = "spring", uses = {BalanceMapper.class})
public interface AccountMapper {

    /**
     * Converts an {@link Account} entity to its corresponding {@link AccountResponse}.
     *
     * @param account The {@link Account} entity to be converted.
     * @return The converted {@link AccountResponse}.
     */
    @Mapping(source = "balance", target = "balance")
    AccountResponse toDto(Account account);

    /**
     * Converts an {@link AccountResponse} to its corresponding {@link Account} entity.
     *
     * @param accountResponse The {@link AccountResponse} to be converted.
     * @return The converted {@link Account} entity.
     */
    @Mapping(source = "balance", target = "balance")
    Account toEntity(AccountResponse accountResponse);
}
