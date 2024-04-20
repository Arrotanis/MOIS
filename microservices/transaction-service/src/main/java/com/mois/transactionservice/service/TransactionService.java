package com.mois.transactionservice.service;

import com.mois.transactionservice.dto.CreateAccountRequest;
import com.mois.transactionservice.dto.TransactionDto;
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
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public void createTransaction(TransactionDto transactionDto, Long sourceAccountId, Long targetAccountId) {
        Transaction transaction = new Transaction();
        transaction.setTransactionNumber(UUID.randomUUID().toString());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setTransactionAmount(transactionDto.getTransactionAmount());

        Optional<Account> optionalSourceAccount = accountRepository.findById(sourceAccountId);
        Optional<Account> optionalTargetAccount = accountRepository.findById(targetAccountId);

        if (optionalSourceAccount.isPresent() && optionalTargetAccount.isPresent()) {
            Account sourceAccount = optionalSourceAccount.get();
            Account targetAccount = optionalTargetAccount.get();

            transaction.setSourceAccount(sourceAccount);
            transaction.setTargetAccount(targetAccount);
            sourceAccount.getSourceTransactions().add(transaction);
            targetAccount.getTargetTransactions().add(transaction);
            sourceAccount.setBalance(sourceAccount.getBalance()-transaction.getTransactionAmount());
            targetAccount.setBalance(targetAccount.getBalance()+transaction.getTransactionAmount());

            // Save the transaction
            transactionRepository.save(transaction);


            // Update both accounts
            accountRepository.save(sourceAccount);
            accountRepository.save(targetAccount);
        } else {
            // Handle the case when either source or target account does not exist
            throw new EntityNotFoundException("Source or target account not found");
        }
    }
    private final WebClient.Builder webClientBuilder;


    public void createAccount(CreateAccountRequest request) {
        Account account = new Account();
        account.setOwnerProfileId(request.getOwnerAccountId());
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
    public List<Account> getAccountsByOwnerProfileId(Long ownerProfileId) {
        return accountRepository.findByOwnerProfileId(ownerProfileId);
    }

    //getTransactionHistory
}
