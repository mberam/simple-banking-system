package com.leapwise.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class TransactionDto {

    private long transactionId;

    private long senderAccountId;

    private long receiverAccountId;

    private BigDecimal amount;

    private int currencyId;

    private String message;

    private LocalDateTime createdAt;
}
