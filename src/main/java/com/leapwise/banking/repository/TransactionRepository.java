package com.leapwise.banking.repository;

import com.leapwise.banking.db.Transaction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {


    List<Transaction> findAllByReceiverAccount_Customer_IdOrSenderAccount_Customer_Id(Long receiverId, Long senderId);

    List<Transaction> findAll(Specification<Transaction> specification, Pageable pageable);

    @Query("SELECT t.amount FROM Transaction t WHERE t.receiverAccount.id = :accountId AND MONTH(t.createdAt) = :month AND YEAR(t.createdAt) = :year")
    List<BigDecimal> findAllBySenderAccountIdAndMonth(@Param("accountId") Long accountId, @Param("month") Integer month, @Param("year") Integer year);

    @Query("SELECT t.amount FROM Transaction t WHERE t.senderAccount.id = :accountId AND MONTH(t.createdAt) = :month AND YEAR(t.createdAt) = :year")
    List<BigDecimal> findAllByReceiverAccountIdAndMonth(@Param("accountId") Long accountId, @Param("month") Integer month, @Param("year") Integer year);
}
