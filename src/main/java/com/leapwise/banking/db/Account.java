package com.leapwise.banking.db;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="\"account\"")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="\"id\"")
    private Long id;

    @Column(name="\"account_number\"")
    private String accountNumber;

    @Column(name="\"account_type\"")
    private String accountType;

    @Column(name="\"balance\"")
    private BigDecimal balance;

    @Column(name="\"past_month_turnover\"")
    private BigDecimal pastMonthTurnover;

    @ManyToOne
    @JoinColumn(name="\"customer_id\"")
    private Customer customer;


}
