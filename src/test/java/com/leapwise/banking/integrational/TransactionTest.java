package com.leapwise.banking.integrational;

import com.leapwise.banking.db.Account;
import com.leapwise.banking.db.Customer;
import com.leapwise.banking.db.Transaction;
import com.leapwise.banking.dto.TransactionDto;
import com.leapwise.banking.repository.AccountRepository;
import com.leapwise.banking.repository.CustomerRepository;
import com.leapwise.banking.repository.TransactionRepository;
import com.leapwise.banking.service.TransactionService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class TransactionTest {

    @Autowired
    TransactionService transactionService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void testGetTransactionHistory() {
        Customer savedCustomer1 = customerRepository.save(Customer.builder()
                .name("John Doe")
                .address("123 Main St")
                .email("mihovilleapwise@gmail.com")
                .phoneNumber("1234567890")
                .build());

        Customer savedCustomer2 = customerRepository.save(Customer.builder()
                .name("Jane Smith")
                .address("456 Oak Ave")
                .email("mihovilleapwise@gmail.com")
                .phoneNumber("9876543210")
                .build());

        Account savedAccount1 = accountRepository.save(Account.builder()
                .accountNumber("123456789")
                .accountType("Savings")
                .balance(new BigDecimal("5000.00"))
                .pastMonthTurnover(new BigDecimal("3000.00"))
                .customer(savedCustomer1)
                .build());

        Account savedAccount2 = accountRepository.save(Account.builder()
                .accountNumber("987654321")
                .accountType("Checking")
                .balance(new BigDecimal("10000.00"))
                .pastMonthTurnover(new BigDecimal("3000.00"))
                .customer(savedCustomer2)
                .build());

        transactionRepository.save(Transaction.builder()
                .senderAccount(savedAccount1)
                .receiverAccount(savedAccount2)
                .amount(new BigDecimal("1000.00"))
                .currencyId(1)
                .message("Payment for services")
                .createdAt(LocalDateTime.now())
                .build());

        transactionRepository.save(Transaction.builder()
                .senderAccount(savedAccount2)
                .receiverAccount(savedAccount1)
                .amount(new BigDecimal("500.00"))
                .currencyId(1)
                .message("Transfer to savings")
                .createdAt(LocalDateTime.now())
                .build());
        List<TransactionDto> transactions = this.transactionService.getTransactionHistory(Pageable.ofSize(100000), 1L,
                Optional.empty(), Optional.empty());
        assertEquals(2, transactions.size());
    }


}

