package com.leapwise.banking.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Random;
@Component
@Log4j2
public class TransactionFileGenerator {

    public void generateTransactionsFile(String filename, int numberOfTransactions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < numberOfTransactions; i++) {
                String transactionData = generateRandomTransactionData();
                writer.write(transactionData);
                writer.newLine();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String generateRandomTransactionData() {
        Random random = new Random();
        long senderAccountId = random.nextLong(4) + 1;
        long receiverAccountId = random.nextLong(4) + 1;
        BigDecimal amount = BigDecimal.valueOf(random.nextDouble() * 1000);
        int currencyId = random.nextInt(5);
        String message = "Transaction message";
        LocalDateTime createdAt = LocalDateTime.now();

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        return String.format("%d;%d;%s;%d;%s;%s",
                senderAccountId, receiverAccountId, formatter.format(amount), currencyId, message, createdAt);
    }
}

