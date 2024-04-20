package com.mois.transactionservice.controller;

import com.mois.transactionservice.dto.AddBalanceDto;
import com.mois.transactionservice.dto.CreateAccountRequestDto;
import com.mois.transactionservice.dto.TransactionDto;
import com.mois.transactionservice.dto.TransferToDepositDto;
import com.mois.transactionservice.model.Account;
import com.mois.transactionservice.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/create-transaction")
    @ResponseStatus(HttpStatus.CREATED)
    public String createTransactionAndAddToAccounts(@RequestBody TransactionDto transactionDto) {
        transactionService.createTransaction(transactionDto);
        return "Transaction Created and Added to Accounts Successfully";
    }

    /*@PostMapping("/create-transaction")
    @ResponseStatus(HttpStatus.CREATED)
    public String createTransactionAndAddToAccounts(@RequestBody TransactionDto transactionDto,
                                                    @RequestParam Long sourceAccountId,
                                                    @RequestParam Long targetAccountId) {
        transactionService.createTransaction(transactionDto, sourceAccountId, targetAccountId);
        return "Transaction Created and Added to Accounts Successfully";
    }*/

    @PostMapping("/create-account")
    @ResponseStatus(HttpStatus.CREATED)
    public String createAccount(@RequestBody CreateAccountRequestDto request){
        transactionService.createAccount(request);
        return "Account Created Successfully";
    }

    @PostMapping("/add-balance")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public String addBalanceToAccount(@RequestBody AddBalanceDto addBalanceDto) {
        transactionService.addBalance(addBalanceDto);
        return "Balance added to account successfully";
    }

    /*public String addBalanceToAccount(@RequestParam Long accountId,
                                      @RequestParam int amountToAdd) {
        transactionService.addBalance(accountId, amountToAdd);
        return "Balance added to account successfully";
    }*/

    @PostMapping("/deposit-transfer")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public String transferToDeposit(@RequestBody TransferToDepositDto transferToDepositDto) {
        transactionService.transferToDeposit(transferToDepositDto);
        return "Balance transferred to deposit successfully";
    }

    @GetMapping("/accounts/{ownerProfileId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAccountsByOwnerProfileId(@PathVariable Long ownerProfileId) {
        return transactionService.getAccountsByOwnerProfileId(ownerProfileId);
    }
}
