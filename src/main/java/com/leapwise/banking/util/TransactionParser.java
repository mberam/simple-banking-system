package com.leapwise.banking.util;

import com.leapwise.banking.db.Account;
import com.leapwise.banking.db.Transaction;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Log4j2
public class TransactionParser {

    public void parseTransactionsFromFile(String filename, ArrayBlockingQueue<List<Transaction>> q, AtomicBoolean isDone, int batchSize) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            List<Transaction> transactions = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                Transaction transaction = parseTransactionData(line);
                transactions.add(transaction);
                if (transactions.size() == batchSize) {
                    q.put(transactions);
                    transactions = new ArrayList<>();
                }
            }
            if (!transactions.isEmpty()) {
                q.put(transactions);
            }
            isDone.set(true);
        } catch (IOException|InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    private Transaction parseTransactionData(String line) {
        String[] parts = line.split(";");
        long senderAccountId = Long.parseLong(parts[0]);
        long receiverAccountId = Long.parseLong(parts[1]);
        BigDecimal amount = new BigDecimal(parts[2]);
        int currencyId = Integer.parseInt(parts[3]);
        String message = parts[4];
        LocalDateTime createdAt = LocalDateTime.parse(parts[5]);

        return Transaction.builder()
                .senderAccount(Account.builder().id(senderAccountId).build())
                .receiverAccount(Account.builder().id(receiverAccountId).build())
                .amount(amount)
                .currencyId(currencyId)
                .message(message)
                .createdAt(createdAt)
                .build();
    }
}

