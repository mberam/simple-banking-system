package com.leapwise.banking.service;

import com.leapwise.banking.db.Account;
import com.leapwise.banking.repository.AccountRepository;
import com.leapwise.banking.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void calculateMonthlyTurnoverForAccount() {
        List<BigDecimal> positiveTransactionAmounts = Arrays.asList(BigDecimal.valueOf(100), BigDecimal.valueOf(200));
        List<BigDecimal> negativeTransactionAmounts = Arrays.asList(BigDecimal.valueOf(50), BigDecimal.valueOf(75));

        Account account = new Account();
        account.setId(1L);

        when(transactionRepository.findAllByReceiverAccountIdAndMonth(1L, 4, 2024)).thenReturn(positiveTransactionAmounts);
        when(transactionRepository.findAllBySenderAccountIdAndMonth(1L, 4, 2024)).thenReturn(negativeTransactionAmounts);

        accountService.calculateMonthlyTurnoverForAccount(account);

        verify(accountRepository, times(1)).save(account);

        assertEquals(BigDecimal.valueOf(175), account.getPastMonthTurnover());
    }
}

