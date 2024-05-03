package com.leapwise.banking.mappers;

import com.leapwise.banking.db.Transaction;
import com.leapwise.banking.dto.TransactionDto;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionMapper {

    public static TransactionDto mapToTransactionDto(Transaction transaction) {
        return TransactionDto.builder()
                .transactionId(transaction.getId())
                .senderAccountId(transaction.getSenderAccount().getId())
                .receiverAccountId(transaction.getReceiverAccount().getId())
                .amount(transaction.getAmount())
                .currencyId(transaction.getCurrencyId())
                .message(transaction.getMessage())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

    public static List<TransactionDto> mapToTransactionDtos(List<Transaction> transactions) {
        return transactions.stream().map(TransactionMapper::mapToTransactionDto).collect(Collectors.toList());
    }

}
