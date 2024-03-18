package com.mois.transactionservice.controller;

import com.mois.transactionservice.dto.TransactionDto;
import com.mois.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createTransactionAndAddToAccounts(@RequestBody TransactionDto transactionDto,
                                                    @RequestParam Long sourceAccountId,
                                                    @RequestParam Long targetAccountId) {
        transactionService.createTransaction(transactionDto, sourceAccountId, targetAccountId);
        return "Transaction Created and Added to Accounts Successfully";
    }
}
