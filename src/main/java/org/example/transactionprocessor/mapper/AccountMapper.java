package org.example.transactionprocessor.mapper;

import org.example.transactionprocessor.entity.Account;
import org.example.transactionprocessor.entity.dto.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BalanceMapper.class})
public interface AccountMapper {

    @Mapping(source = "balance", target = "balance")
    AccountDto toDto(Account account);

    @Mapping(source = "balance", target = "balance")
    Account toEntity(AccountDto accountDto);
}
