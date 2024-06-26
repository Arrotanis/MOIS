package com.mois.transactionservice.service;

import com.mois.transactionservice.dto.AddBalanceDto;
import com.mois.transactionservice.dto.CreateAccountRequestDto;
import com.mois.transactionservice.dto.TransactionDto;
import com.mois.transactionservice.dto.TransferToDepositDto;
import com.mois.transactionservice.model.Account;
import com.mois.transactionservice.model.Transaction;
import com.mois.transactionservice.repository.AccountRepository;
import com.mois.transactionservice.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public void createTransaction(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setTransactionNumber(UUID.randomUUID().toString());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setTransactionAmount(transactionDto.getTransactionAmount());

        Optional<Account> optionalSourceAccount = accountRepository.findById(transactionDto.getSourceAccount());
        Optional<Account> optionalTargetAccount = accountRepository.findById(transactionDto.getTargetAccount());

        if (optionalSourceAccount.isPresent() && optionalTargetAccount.isPresent() && transaction.getTransactionAmount() > 0) {
            Account sourceAccount = optionalSourceAccount.get();
            Account targetAccount = optionalTargetAccount.get();

            // Check if the source account has sufficient balance
            if (sourceAccount.getBalance() < transaction.getTransactionAmount()) {
                throw new IllegalArgumentException("Insufficient balance in the source account");
            }

            sourceAccount.setBalance(sourceAccount.getBalance()-transaction.getTransactionAmount());
            targetAccount.setBalance(targetAccount.getBalance()+transaction.getTransactionAmount());

            transaction.setSourceAccount(sourceAccount.getId());
            transaction.setTargetAccount(targetAccount.getId());

            sourceAccount.getSourceTransactions().add(transaction);
            targetAccount.getTargetTransactions().add(transaction);

            accountRepository.save(sourceAccount);
            accountRepository.save(targetAccount);

            transactionRepository.save(transaction);



        } else {
            // Handle the case when either source or target account does not exist
            throw new EntityNotFoundException("Source or target account not found, or amount is even or less than 0");
        }
    }
    private final WebClient.Builder webClientBuilder;


    public void createAccount(CreateAccountRequestDto request) {
        Account account = new Account();
        account.setOwnerProfileId(request.getOwnerAccountId());
        accountRepository.save(account);
    }

    @Transactional
    public void addBalance(AddBalanceDto addBalanceDto) {
        Optional<Account> optionalAccount = accountRepository.findById(addBalanceDto.getTargetAccountId());
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            int currentBalance = account.getBalance();
            int newBalance = currentBalance + addBalanceDto.getAddBalanceAmount();
            account.setBalance(newBalance);


            Transaction transaction = new Transaction();
            transaction.setTargetAccount(account.getId());
            transaction.setTransactionAmount(addBalanceDto.getAddBalanceAmount());
            transaction.setDescription("Income from outside source");
            transaction.setTransactionNumber(UUID.randomUUID().toString());
            account.getTargetTransactions().add(transaction);

            transactionRepository.save(transaction);
            accountRepository.save(account);
        } else {
            throw new EntityNotFoundException("Account not found with ID: " + addBalanceDto.getTargetAccountId());
        }
    }

    @Transactional
    public void addBalanceFromDeposit(AddBalanceDto addBalanceDto) {
        Optional<Account> optionalAccount = accountRepository.findById(addBalanceDto.getTargetAccountId());
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            int currentBalance = account.getBalance();
            int newBalance = currentBalance + addBalanceDto.getAddBalanceAmount();
            account.setBalance(newBalance);


            Transaction transaction = new Transaction();
            transaction.setTargetAccount(account.getId());
            transaction.setTransactionAmount(addBalanceDto.getAddBalanceAmount());
            transaction.setDescription("Term deposit interest earnings");
            transaction.setTransactionNumber(UUID.randomUUID().toString());
            account.getTargetTransactions().add(transaction);

            transactionRepository.save(transaction);
            accountRepository.save(account);
        } else {
            throw new EntityNotFoundException("Account not found with ID: " + addBalanceDto.getTargetAccountId());
        }
    }

    public void transferToDeposit(TransferToDepositDto transferToDepositDto) {
        Optional<Account> optionalAccount = accountRepository.findById(transferToDepositDto.getAccountId());
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            int currentBalance = account.getBalance();
            int newBalance = currentBalance - transferToDepositDto.getAmountToTransfer();
            account.setBalance(newBalance);


            Transaction transaction = new Transaction();
            transaction.setSourceAccount(account.getId());
            transaction.setTransactionAmount(transferToDepositDto.getAmountToTransfer());
            transaction.setDescription("Transfer to deposit");
            transaction.setTransactionNumber(UUID.randomUUID().toString());
            account.getTargetTransactions().add(transaction);

            accountRepository.save(account);
            transactionRepository.save(transaction);
        }
    }
    public List<Account> getAccountsByOwnerProfileId(Long ownerProfileId) {

        return accountRepository.findByOwnerProfileId(ownerProfileId);
    }

    public int getBalanceFromAccountById(Long accountId){
        Optional<Account> account = accountRepository.findById(accountId);
        int currentAccountBalance = account.get().getBalance();

        return currentAccountBalance;
    }
}
