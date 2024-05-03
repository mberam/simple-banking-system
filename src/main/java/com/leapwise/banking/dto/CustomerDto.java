package com.leapwise.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class CustomerDto {

    private long id;

    private String name;

    private String address;

    private String email;

    private String phoneNumber;

    private BigDecimal balance;
}