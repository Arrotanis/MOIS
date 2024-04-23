package com.mois.depositservice.service;

import com.mois.depositservice.dto.AddBalanceDto;
import com.mois.depositservice.dto.CreateDepositDto;
import com.mois.depositservice.dto.TransferToDepositDto;
import com.mois.depositservice.model.Deposit;
import com.mois.depositservice.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DepositService {
    private final DepositRepository depositRepository;


    private final WebClient.Builder webClientBuilder;

    //nejaky schedule manager
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void createDeposit(CreateDepositDto createDepositDto) {
        Deposit deposit = new Deposit();
        deposit.setLinkedAccountId(createDepositDto.getLinkedAccountId());
        deposit.setInterestRate(4);
        deposit.setOwnerProfileId(createDepositDto.getOwnerProfileId());
        deposit.setDepositedBalance(createDepositDto.getDepositedBalance());
        deposit.setStartDate(LocalDateTime.now());
        deposit.setEndDate(LocalDateTime.now().plusMinutes(1));
        depositRepository.save(deposit);

        TransferToDepositDto transferToDepositDto = new TransferToDepositDto();
        transferToDepositDto.setAccountId(createDepositDto.getLinkedAccountId());
        transferToDepositDto.setAmountToTransfer(createDepositDto.getDepositedBalance());

        float totalSum = createDepositDto.getDepositedBalance() * deposit.getInterestRate();
        // Schedule the method execution
        //executeAtDateTime(this::myScheduledMethod, deposit.getEndDate());
        executeAtDateTime(() -> myScheduledMethod(createDepositDto.getLinkedAccountId(), createDepositDto.getDepositedBalance(), totalSum), deposit.getEndDate());

        Mono<String> responseMono = webClientBuilder.build().post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("transaction-service")
                        .path("api/transaction/deposit-transfer")
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(transferToDepositDto))
                .retrieve()
                .bodyToMono(String.class);

        // Subscribe to the response asynchronously
        responseMono.subscribe(responseBody -> {
            // Handle the response asynchronously
            System.out.println("Response Body: " + responseBody);
        }, error -> {
            // Handle errors asynchronously
            System.err.println("Error occurred: " + error.getMessage());
        });
        System.out.println("createDeposit guchi");
    }

    public void executeAtDateTime(Runnable task, LocalDateTime executionTime) {
        LocalDateTime now = LocalDateTime.now();
        long delay = Duration.between(now, executionTime).toMillis();
        if (delay <= 0) {
            throw new IllegalArgumentException("Execution time must be in the future");
        }
        scheduler.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    // Example method to be executed at a specific date and time
        public void myScheduledMethod(Long linkedAccountId, int depositedBalance, float totalSum) {




        AddBalanceDto addBalanceDto = new AddBalanceDto();
        addBalanceDto.setTargetAccountId(linkedAccountId);
        addBalanceDto.setAddBalanceAmount((int)totalSum);

        Mono<String> responseMono = webClientBuilder.build().post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("transaction-service")
                        .path("api/transaction/add-balance")
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(addBalanceDto))
                .retrieve()
                .bodyToMono(String.class);

        // Subscribe to the response asynchronously
        responseMono.subscribe(responseBody -> {
            // Handle the response asynchronously
            System.out.println("Response Body: " + responseBody);
        }, error -> {
            // Handle errors asynchronously
            System.err.println("Error occurred: " + error.getMessage());
        });

        System.out.println("myScheduledMethod guchi");
    }



    //returnDeposit
}
