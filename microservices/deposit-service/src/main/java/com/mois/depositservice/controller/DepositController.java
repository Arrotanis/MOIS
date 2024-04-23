package com.mois.depositservice.controller;

import com.mois.depositservice.dto.CreateDepositDto;
import com.mois.depositservice.model.Deposit;
import com.mois.depositservice.service.DepositService;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deposit")
@RequiredArgsConstructor
public class DepositController {
    private final DepositService depositService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createAccount(@RequestBody CreateDepositDto createDepositDto){
        depositService.createDeposit(createDepositDto);
        return "Deposit Created Successfully";
    }

    @GetMapping("/{linkedAccountId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Deposit> getDepositsByLinkedAccountId(@PathVariable Long linkedAccountId){
        return depositService.getAllLinkedAccountDeposits(linkedAccountId);
    }
}
