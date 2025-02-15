package org.example.transactionprocessor.model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

//TODO use DTO instead of entity
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountFrom;
    private String accountTo;
    private Double amount;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        this.timestamp = ZonedDateTime.now();
    }

    public Transaction() {
    }

    public Transaction(String accountFrom, String accountTo, Double amount) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public String getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(String accountFrom) {
        this.accountFrom = accountFrom;
    }

    public String getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(String accountTo) {
        this.accountTo = accountTo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", accountTo='" + accountTo + '\'' +
                ", accountFrom='" + accountFrom + '\'' +
                ", id=" + id +
                '}';
    }
}
