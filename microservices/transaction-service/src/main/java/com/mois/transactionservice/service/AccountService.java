package com.mois.transactionservice.service;

import com.mois.transactionservice.dto.AccountRequest;
import com.mois.transactionservice.dto.TransactionDto;
import com.mois.transactionservice.model.Account;
import com.mois.transactionservice.model.Transaction;
import com.mois.transactionservice.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final WebClient webClient;


    public void createAccount() {
        Account account = new Account();
        accountRepository.save(account);
    }

    @Transactional
    public void addBalance(Long accountId, int amountToAdd) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            int currentBalance = account.getBalance();
            int newBalance = currentBalance + amountToAdd;
            account.setBalance(newBalance);
            accountRepository.save(account);

            //createTransaction...
        } else {
            throw new EntityNotFoundException("Account not found with ID: " + accountId);
        }
    }


    //getTransactionHistory






      /* public void createAccount(AccountRequest accountRequest) {
        Account account = new Account();

        List<Transaction> transactionList = accountRequest.getTransactionDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        account.setTransactionList(transactionList);
        accountRepository.save(account);
    }*/

    private Transaction mapToDto(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setTransactionNumber(transactionDto.getTransactionNumber());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setTargetAccount(transactionDto.getTargetAccount());
        transaction.setTargetAccount(transactionDto.getTargetAccount());
        return transaction;
    }
}
