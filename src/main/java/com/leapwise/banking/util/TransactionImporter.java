package com.leapwise.banking.util;

import com.leapwise.banking.db.Transaction;
import com.leapwise.banking.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Log4j2
@AllArgsConstructor
public class TransactionImporter {

    private final TransactionService transactionService;
    private final TransactionParser transactionParser;

    @Async
    public void importTransactions() {
        int batchSize = 3000;
        int numThreads = 3;
        ArrayBlockingQueue<List<Transaction>> q = new ArrayBlockingQueue<>(10);
        AtomicBoolean isDone = new AtomicBoolean(false);

        CompletableFuture.runAsync(() -> {
            transactionParser.parseTransactionsFromFile("transactions.txt", q, isDone, batchSize);
        });

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        try {
            List<CompletableFuture<Void>> batchFutures = new ArrayList<>();
            for (int i = 0; i < numThreads; i++) {
                CompletableFuture<Void> batchFuture = CompletableFuture.runAsync(() -> {
                    try {
                        do {
                            List<Transaction> transactions = q.take();
                            transactionService.saveAllInTransaction(transactions);
                        } while (!isDone.get() || !q.isEmpty());
                    } catch(InterruptedException e) {
                        log.error("Error importing transactions: {}", e.getMessage(), e);
                    }
                }, executorService);
                batchFutures.add(batchFuture);
            }
            CompletableFuture.allOf(batchFutures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            log.error("Error importing transactions: {}", e.getMessage(), e);
        } finally {
            executorService.shutdown();
            log.info("Transaction import completed successfully.");
        }
    }
}

