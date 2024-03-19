package com.mois.transactionservice.controller;

import com.mois.transactionservice.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createAccount(@RequestParam Long ownerAccountId){
        accountService.createAccount(ownerAccountId);
        return "Account Created Successfully";
    }

    @PostMapping("/add-balance")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public String addBalanceToAccount(@RequestParam Long accountId,
                                      @RequestParam int amountToAdd) {
        accountService.addBalance(accountId, amountToAdd);
        return "Balance added to account successfully";
    }

    @PostMapping("/deposit-transfer")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public String transferToDeposit(@RequestParam Long accountId,
                                      @RequestParam int amountToTransfer) {
        accountService.transferToDeposit(accountId, amountToTransfer);
        return "Balance transferred to deposit successfully";
    }
}
