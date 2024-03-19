package com.mois.depositservice.controller;

import com.mois.depositservice.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deposit")
@RequiredArgsConstructor
public class DepositController {
    private final DepositService depositService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createAccount(@RequestParam Long linkedAccountId, @RequestParam Long ownerProfileId, @RequestParam int depositedBalance){
        depositService.createDeposit(linkedAccountId, ownerProfileId, depositedBalance);
        return "Deposit Created Successfully";
    }
}
