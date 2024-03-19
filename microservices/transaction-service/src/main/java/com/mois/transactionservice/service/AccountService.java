package com.mois.transactionservice.service;

import com.mois.transactionservice.model.Account;
import com.mois.transactionservice.model.Transaction;
import com.mois.transactionservice.repository.AccountRepository;
import com.mois.transactionservice.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final WebClient webClient;


    public void createAccount(Long ownerProfileId) {
        Account account = new Account();
        account.setOwnerProfileId(ownerProfileId);
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

            Transaction transaction = new Transaction();
            transaction.setTargetAccount(account);
            transaction.setTransactionAmount(amountToAdd);
            transaction.setDescription("Income from outside source");
            transaction.setTransactionNumber(UUID.randomUUID().toString());
            account.getTargetTransactions().add(transaction);
            transactionRepository.save(transaction);
        } else {
            throw new EntityNotFoundException("Account not found with ID: " + accountId);
        }
    }


    public void transferToDeposit(Long accountId, int amountToTransfer) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            int currentBalance = account.getBalance();
            int newBalance = currentBalance - amountToTransfer;
            account.setBalance(newBalance);
            accountRepository.save(account);

            Transaction transaction = new Transaction();
            transaction.setSourceAccount(account);
            transaction.setTransactionAmount(amountToTransfer);
            transaction.setDescription("Transfer to deposit");
            transaction.setTransactionNumber(UUID.randomUUID().toString());
            account.getTargetTransactions().add(transaction);
            transactionRepository.save(transaction);
        }
    }


        //getTransactionHistory
}
