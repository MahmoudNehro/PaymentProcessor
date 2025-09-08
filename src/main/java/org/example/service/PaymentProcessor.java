package org.example.service;

import org.example.db.Constants.TransactionType;
import org.example.db.transactions.Transaction;
import org.example.db.transactions.TransactionRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PaymentProcessor {
    private static final long avgWaitTimeNs = 90_000_000;
    private static final long avgServiceTimeNs = 1_000_000;
    private static final int noOfThreads = ThreadPoolSizer.calculateOptimalThreads(avgWaitTimeNs, avgServiceTimeNs);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(noOfThreads);
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentProcessor.class.getName());

    private final TransactionRepo transactionRepo = new TransactionRepo();

    private PaymentProcessor() {
    }

    private static final class InstanceHolder {
        private static final PaymentProcessor instance = new PaymentProcessor();
    }

    public static PaymentProcessor getInstance() {
        return InstanceHolder.instance;
    }

    public void makeTransaction(Transaction transaction) throws UnsupportedOperationException {
        if (transaction.type() != TransactionType.CREDIT && transaction.type() != TransactionType.DEBIT) {
            throw new UnsupportedOperationException("Invalid transaction type");
        }

        executorService.submit(() -> {
            // simulating calling third party
            sleep();
            LOGGER.info("Making transaction on thread {} ", Thread.currentThread().getName());
            transactionRepo.makeTransaction(transaction);
        });
    }

    private static void sleep() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
