package com.mois.transactionservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mois.transactionservice.dto.AddBalanceDto;
import com.mois.transactionservice.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mois.transactionservice.dto.AddBalanceDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
public class TestClass {

    @Test
    public void testMe(){
        System.out.println("Print me");
    }
/*
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddBalanceToAccount() throws Exception {
        // Given
        AddBalanceDto addBalanceDto = new AddBalanceDto();
        addBalanceDto.setTargetAccountId((long)1);

        // When
        mockMvc.perform(post("/add-balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addBalanceDto)))
                .andExpect(status().isOk());

        // Then
        verify(accountRepository).findByOwnerProfileId((long)1);
    }*/
}

/*
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
* WebClient.Builder webClientBuilder = null;

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
        });*/