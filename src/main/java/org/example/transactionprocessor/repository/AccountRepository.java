package org.example.transactionprocessor.repository;

import org.example.transactionprocessor.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Account} entities.
 * <p>
 * Provides database access methods for retrieving and managing account data.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
}
