package com.leapwise.banking.controller;

import com.leapwise.banking.db.Transaction;
import com.leapwise.banking.dto.TransactionDto;
import com.leapwise.banking.dto.TransactionRequestDto;
import com.leapwise.banking.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("")
    Long processTransaction(@RequestBody TransactionRequestDto transactionRequestDto) {
        return this.transactionService.processTransaction(transactionRequestDto);
    }

    @GetMapping("/history/{id}")
    List<TransactionDto> getTransactionHistory(@PageableDefault Pageable pageable,
                                               @PathVariable Long id,
                                               @RequestParam(required = false) Optional<Long> receiverAccountId,
                                               @RequestParam(required = false) Optional<Long> senderAccountId) {
        return this.transactionService.getTransactionHistory(pageable, id, receiverAccountId, senderAccountId);
    }
}
