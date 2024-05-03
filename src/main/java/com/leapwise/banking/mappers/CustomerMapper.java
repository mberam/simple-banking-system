package com.leapwise.banking.mappers;

import com.leapwise.banking.db.Customer;
import com.leapwise.banking.dto.CustomerDto;

import java.math.BigDecimal;

public class CustomerMapper {

    public static CustomerDto mapToCustomerDto(Customer customer, BigDecimal balance) {
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .address(customer.getAddress())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .balance(balance)
                .build();
    }
}
