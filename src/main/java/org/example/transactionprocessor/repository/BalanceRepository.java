package org.example.transactionprocessor.repository;

import org.example.transactionprocessor.entity.Account;
import org.example.transactionprocessor.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Optional<Balance> findByAccount(Account account);
}
