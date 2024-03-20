package com.mois.depositservice.service;

import com.mois.depositservice.model.Deposit;
import com.mois.depositservice.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DepositService {
    private final DepositRepository depositRepository;

    private final WebClient.Builder webClientBuilder;

    public void createDeposit(Long linkedAccountId, Long ownerProfileId, int depositedBalance) {
        Deposit deposit = new Deposit();
        deposit.setLinkedAccountId(linkedAccountId);
        deposit.setInterestRate(4);
        deposit.setOwnerProfileId(ownerProfileId);
        deposit.setDepositedBalance(depositedBalance);
        depositRepository.save(deposit);

        Mono<String> responseMono = webClientBuilder.build().post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("transaction-service")
                        .path("/api/account/deposit-transfer")
                        .queryParam("accountId", linkedAccountId)
                        .queryParam("amountToTransfer", depositedBalance)
                        .build())
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
    }

    //returnDeposit
}
