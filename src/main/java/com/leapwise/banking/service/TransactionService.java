package com.leapwise.banking.service;

import com.leapwise.banking.db.Account;
import com.leapwise.banking.db.Customer;
import com.leapwise.banking.db.Transaction;
import com.leapwise.banking.dto.TransactionDto;
import com.leapwise.banking.dto.TransactionRequestDto;
import com.leapwise.banking.exceptions.BankingException;
import com.leapwise.banking.mappers.TransactionMapper;
import com.leapwise.banking.repository.AccountRepository;
import com.leapwise.banking.repository.CustomerRepository;
import com.leapwise.banking.repository.TransactionRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class TransactionService {

    public final TransactionRepository transactionRepository;
    public final AccountRepository accountRepository;
    public final CustomerRepository customerRepository;
    private final EmailService emailService;

    @Transactional
    public List<TransactionDto> getTransactionHistory(Pageable pageable, Long id, Optional<Long> receiverAccountId, Optional<Long> senderAccountId) {
        Optional<Customer> customerOptional = this.customerRepository.findById(id);
        if (customerOptional.isEmpty()) {
            throw new BankingException("Customer not found.", "Customer not found.", HttpStatus.NOT_FOUND);
        }
        Customer customer = customerOptional.get();
        List<Long> accountIds = customer.getAccounts().stream()
                .map(Account::getId)
                .toList();
        Specification<Transaction> specification = this.createQuerySpecification(accountIds, senderAccountId, receiverAccountId);
        List<Transaction> transactions = this.transactionRepository.findAll(specification, pageable);
        return TransactionMapper.mapToTransactionDtos(transactions);
    }


    @Transactional
    public Long processTransaction(TransactionRequestDto transactionRequestDto) {
        Optional<Account> senderAccountOptional = this.accountRepository.findByIdWithPessimisticWriteLock(transactionRequestDto.getSenderAccountId());
        Optional<Account> receiverAccountOptional = this.accountRepository.findByIdWithPessimisticWriteLock(transactionRequestDto.getReceiverAccountId());

        if (senderAccountOptional.isPresent() && receiverAccountOptional.isPresent()) {
            Account senderAccount = senderAccountOptional.get();
            Account receiverAccount = receiverAccountOptional.get();

            BigDecimal amount = transactionRequestDto.getAmount();
            if (senderAccount.getBalance().compareTo(amount) < 0) {
                throw new BankingException("Insufficient founds.", "Insufficient founds.", HttpStatus.BAD_REQUEST);
            }

            BigDecimal senderOldBalance = senderAccount.getBalance();
            BigDecimal receiverOldBalance = receiverAccount.getBalance();

            BigDecimal senderNewBalance = senderOldBalance.subtract(amount);
            BigDecimal receiverNewBalance = receiverOldBalance.add(amount);

            senderAccount.setBalance(senderNewBalance);
            receiverAccount.setBalance(receiverNewBalance);

            this.accountRepository.save(senderAccount);
            this.accountRepository.save(receiverAccount);

            Transaction transaction = new Transaction();
            transaction.setSenderAccount(senderAccount);
            transaction.setReceiverAccount(receiverAccount);
            transaction.setAmount(amount);
            transaction.setCurrencyId(transactionRequestDto.getCurrencyId());
            transaction.setMessage(transactionRequestDto.getMessage());
            transaction.setCreatedAt(LocalDateTime.now());

            Transaction savedTransaction = this.transactionRepository.save(transaction);

            MimeMessage messageSenderSuccess = this.emailService.buildMessage(savedTransaction, senderAccount, true, senderOldBalance, true);
            this.emailService.send(messageSenderSuccess);
            MimeMessage messageReceiverSuccess = this.emailService.buildMessage(savedTransaction, receiverAccount, true, receiverOldBalance, false);
            this.emailService.send(messageReceiverSuccess);

            return savedTransaction.getId();
        } else {
            throw new BankingException("Account not present.", "Account not present.", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public void saveAllInTransaction(List<Transaction> transactions) {
        transactionRepository.saveAll(transactions);
        log.info("Saved batch of {}", transactions.size());
    }

    private Specification<Transaction> createQuerySpecification (List<Long> accountIds, Optional<Long> senderAccountId, Optional<Long> receiverAccountId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Predicate accountIdsPredicate = criteriaBuilder.or(
                    root.get("senderAccount").get("id").in(accountIds),
                    root.get("receiverAccount").get("id").in(accountIds)
            );
            predicates.add(accountIdsPredicate);
            senderAccountId.ifPresent(aLong -> predicates.add(criteriaBuilder.equal(root.get("senderAccount").get("id"), aLong)));
            receiverAccountId.ifPresent(aLong -> predicates.add(criteriaBuilder.equal(root.get("receiverAccount").get("id"), aLong)));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

}
