package com.leapwise.banking.service;

import com.leapwise.banking.db.Account;
import com.leapwise.banking.db.Customer;
import com.leapwise.banking.dto.CustomerDto;
import com.leapwise.banking.exceptions.BankingException;
import com.leapwise.banking.mappers.CustomerMapper;
import com.leapwise.banking.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerDto getCustomerById(Long id) {
        Optional<Customer> customerOptional = this.customerRepository.findById(id);

        if (customerOptional.isEmpty()) {
            throw new BankingException("Customer not found.", "Customer not found.", HttpStatus.NOT_FOUND);
        }
        Customer customer = customerOptional.get();
        List<Account> accounts = customer.getAccounts();
        BigDecimal totalBalance = accounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return CustomerMapper.mapToCustomerDto(customer, totalBalance);
    }
}
