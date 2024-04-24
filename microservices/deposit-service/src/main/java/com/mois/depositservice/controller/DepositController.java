package com.mois.depositservice.controller;

import com.mois.depositservice.dto.CreateDepositDto;
import com.mois.depositservice.model.Deposit;
import com.mois.depositservice.service.DepositService;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @GetMapping("/get-balance/test/{accountId}")
    public int localDateTime(@PathVariable Long accountId){



        LocalDateTime now = LocalDateTime.now().plusMinutes(5).plusHours(2);
        int hour = now.getHour();
        int minutes = now.getMinute();
        int seconds = now.getSecond();
        String date = "2024-04-24";
        String formatedHour = String.format("%02d", hour);
        String formatedMinutes = String.format("%02d", minutes);
        String formatedSeconds = String.format("%02d", seconds);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime customDate = LocalDateTime.parse(date+"T"+formatedHour+":"+formatedMinutes+":"+formatedSeconds, formatter);
        //LocalDateTime customDate = LocalDateTime.of(year,month,day,hour,minutes);
        //return customDate.toString()+"asd"+formatedHour+formatedMinutes+formatedSeconds;
        int currentBalance = 0;
        //currentBalance = depositService.deleteMeLater(accountId).block();
        return currentBalance;
    }
}
