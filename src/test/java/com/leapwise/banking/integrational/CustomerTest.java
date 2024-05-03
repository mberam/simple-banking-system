package com.leapwise.banking.integrational;

import com.leapwise.banking.db.Customer;
import com.leapwise.banking.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class CustomerTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void saveCustomerTest() {
        Customer customer1 = Customer.builder()
                .name("John Doe")
                .address("123 Main St")
                .email("mihovilleapwise@gmail.com")
                .phoneNumber("1234567890")
                .build();
        Customer savedCustomer = customerRepository.save(customer1);
        Optional<Customer> foundCustomer= customerRepository.findById(savedCustomer.getId());
        assertNotNull(foundCustomer);
        assertEquals(1, customerRepository.count());
    }
}
