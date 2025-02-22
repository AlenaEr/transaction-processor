package org.example.transactionprocessor.repository;

import org.example.transactionprocessor.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Transaction} entities.
 * <p>
 * Provides CRUD operations for transactions using Spring Data JPA.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
