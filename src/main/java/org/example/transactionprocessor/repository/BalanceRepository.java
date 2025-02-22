package org.example.transactionprocessor.repository;

import org.example.transactionprocessor.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Balance} entities.
 * <p>
 * Provides CRUD operations for balance data using Spring Data JPA.
 */
public interface BalanceRepository extends JpaRepository<Balance, Long> {

}
