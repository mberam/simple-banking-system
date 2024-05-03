package com.leapwise.banking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDto {

    @NotBlank(message = "Sender Account Id is mandatory")
    private Long senderAccountId;

    @NotBlank(message = "Receiver Account Id is mandatory")
    private Long receiverAccountId;

    @NotBlank(message = "Amount is mandatory")
    private BigDecimal amount;

    @NotBlank(message = "Currency Id is mandatory")
    private int currencyId;

    private String message;
}
