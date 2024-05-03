package com.leapwise.banking.db;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "\"transaction\"")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="\"id\"")
    private Long id;

    @ManyToOne
    @JoinColumn(name="\"sender_account_id\"")
    private Account senderAccount;

    @ManyToOne
    @JoinColumn(name="\"receiver_account_id\"")
    private Account receiverAccount;

    @Column(name="\"amount\"")
    private BigDecimal amount;

    @Column(name="\"currency_id\"")
    private int currencyId;

    @Column(name="\"message\"")
    private String message;

    @Column(name="\"created_at\"")
    private LocalDateTime createdAt;

}
