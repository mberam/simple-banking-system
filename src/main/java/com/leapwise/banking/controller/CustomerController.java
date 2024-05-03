package com.leapwise.banking.controller;

import com.leapwise.banking.dto.CustomerDto;
import com.leapwise.banking.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("customer")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{id}")
    CustomerDto getCustomerById(@PathVariable Long id) { return this.customerService.getCustomerById(id); }
}
