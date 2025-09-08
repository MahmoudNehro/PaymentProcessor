package org.example.db.transactions;

import org.example.db.Constants.TransactionType;
import org.example.db.Constants.PaymentStatus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public record Transaction(BigInteger transactionId, String accountNumber, BigInteger accountId, BigDecimal amount,
                          String currency, PaymentStatus status, TransactionType type, UUID referenceId,
                          Timestamp createdAt, Timestamp updatedAt) {

    public Transaction(String accountNumber, BigDecimal amount,
                       String currency, TransactionType type) {
        this(null,
                accountNumber,
                null,
                amount,
                currency,
                PaymentStatus.PENDING,
                type,
                UUID.randomUUID(),
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now())
        );
    }
}
