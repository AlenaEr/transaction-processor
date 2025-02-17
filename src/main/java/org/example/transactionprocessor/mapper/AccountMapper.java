package org.example.transactionprocessor.mapper;

import org.example.transactionprocessor.entity.Account;
import org.example.transactionprocessor.entity.dto.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BalanceMapper.class})
public interface AccountMapper {

    // Здесь мы маппируем поле "balance" из сущности Account в "balance" в AccountDto
    @Mapping(source = "balance", target = "balance")  // balance -> balance
    AccountDto toDto(Account account);

    // Здесь мы маппируем "balanceDto" из AccountDto в поле "balance" в сущности Account
    @Mapping(source = "balance", target = "balance")  // balanceDto -> balance
    Account toEntity(AccountDto accountDto);
}
