package com.mois.transactionservice.controller;

import com.mois.transactionservice.dto.AccountRequest;
import com.mois.transactionservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createAccount(){
        accountService.createAccount();
        return "Account Created Successfully";
    }
}
