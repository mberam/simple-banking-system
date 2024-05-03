package com.leapwise.banking.service;

import com.leapwise.banking.db.Account;
import com.leapwise.banking.db.Transaction;
import com.leapwise.banking.repository.AccountRepository;
import com.leapwise.banking.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Log4j2
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void calculateMonthlyTurnover() {
        log.info("Scheduled task for calculating monthly account turnover started.");
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            calculateMonthlyTurnoverForAccount(account);
        }
        log.info("Scheduled task for calculating monthly account turnover done.");
    }

    public void calculateMonthlyTurnoverForAccount(Account account) {
        int previousMonth = LocalDate.now().minusMonths(1).getMonth().getValue();
        int year = LocalDate.now().getYear();
        if (LocalDate.now().getMonth().getValue() == 1) {
            previousMonth = 12;
            year--;
        }

        List<BigDecimal> positiveTransactionAmounts = this.transactionRepository.findAllByReceiverAccountIdAndMonth(account.getId(), previousMonth, year);
        List<BigDecimal> negativeTransactionAmounts = this.transactionRepository.findAllBySenderAccountIdAndMonth(account.getId(), previousMonth, year);

        BigDecimal positiveTurnover = positiveTransactionAmounts.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal negativeTurnover = negativeTransactionAmounts.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal monthlyTurnover = positiveTurnover.subtract(negativeTurnover);
        account.setPastMonthTurnover(monthlyTurnover);
        accountRepository.save(account);
    }

}
