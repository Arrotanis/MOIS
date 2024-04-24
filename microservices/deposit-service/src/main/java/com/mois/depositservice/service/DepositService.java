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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DepositService {
    private final DepositRepository depositRepository;


    private final WebClient.Builder webClientBuilder;


    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void createDeposit(CreateDepositDto createDepositDto) {
        System.out.println("Balance: "+createDepositDto.getDepositedBalance()+", owner profile id: "+createDepositDto.getOwnerProfileId()+", linked account id:"+createDepositDto.getLinkedAccountId()+", Datum:"+createDepositDto.getEndDateTime());
        int currentAccountBalance = getAccountBalance(createDepositDto.getLinkedAccountId()).block();
        System.out.println("Dostal jsem balance z transaction service: "+currentAccountBalance);
        if (currentAccountBalance < createDepositDto.getDepositedBalance()) {
            throw new RuntimeException("Insufficient funds in the account.");
        }
        LocalDateTime now = LocalDateTime.now().plusMinutes(1);
        int hour = now.getHour();
        int minutes = now.getMinute();
        int seconds = now.getSecond();
        String date = createDepositDto.getEndDateTime();
        String formatedHour = String.format("%02d", hour);
        String formatedMinutes = String.format("%02d", minutes);
        String formatedSeconds = String.format("%02d", seconds);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime customDate = LocalDateTime.parse(date+"T"+formatedHour+":"+formatedMinutes+":"+formatedSeconds, formatter);

        Deposit deposit = new Deposit();
        deposit.setLinkedAccountId(createDepositDto.getLinkedAccountId());


        long countSeconds = Duration.between(LocalDateTime.now(),customDate).toSeconds();
        float calculateInterest = (float)((((0.1/365)*countSeconds)+1));
        System.out.println("Interest je: "+calculateInterest+", napocitany sekundy: "+countSeconds);
        deposit.setInterestRate(calculateInterest);
        deposit.setOwnerProfileId(createDepositDto.getOwnerProfileId());
        deposit.setDepositedBalance(createDepositDto.getDepositedBalance());
        deposit.setStartDate(LocalDateTime.now());
        deposit.setEndDate(customDate);
        //deposit.setEndDate(LocalDateTime.now().plusMinutes(1));
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
                        .path("api/transaction/add-balance-deposit")
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

    public List<Deposit> getAllLinkedAccountDeposits(Long linkedAccountId){
        return depositRepository.findDepositByLinkedAccountId(linkedAccountId);
    }

    public Mono<Integer> getAccountBalance(Long accountId){

        return webClientBuilder.build().get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("transaction-service")
                        .path("api/transaction/get-balance/" + accountId)
                        .build())
                .retrieve()
                .bodyToMono(Integer.class);
    }
}
