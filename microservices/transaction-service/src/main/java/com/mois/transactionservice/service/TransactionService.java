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
        System.out.println("Davan add lanace na ucet !!!");
        System.out.println("Na acc: "+ addBalanceDto.getTargetAccountId()+", tolik balance:"+addBalanceDto.getAddBalanceAmount());
        Optional<Account> optionalAccount = accountRepository.findById(addBalanceDto.getTargetAccountId());
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            int currentBalance = account.getBalance();
            int newBalance = currentBalance + addBalanceDto.getAddBalanceAmount();
            account.setBalance(newBalance);
            accountRepository.save(account);

            Transaction transaction = new Transaction();
            transaction.setTargetAccount(account);
            transaction.setTransactionAmount(addBalanceDto.getAddBalanceAmount());
            transaction.setDescription("Income from outside source");
            transaction.setTransactionNumber(UUID.randomUUID().toString());
            account.getTargetTransactions().add(transaction);
            transactionRepository.save(transaction);
            System.out.println("addBalance guchi");
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
            accountRepository.save(account);

            Transaction transaction = new Transaction();
            transaction.setSourceAccount(account);
            transaction.setTransactionAmount(transferToDepositDto.getAmountToTransfer());
            transaction.setDescription("Transfer to deposit");
            transaction.setTransactionNumber(UUID.randomUUID().toString());
            account.getTargetTransactions().add(transaction);
            transactionRepository.save(transaction);

            System.out.println("transferToDeposit guchi");
        }
    }
    public List<Account> getAccountsByOwnerProfileId(Long ownerProfileId) {
        List<Account> akounty;
        akounty = accountRepository.findByOwnerProfileId(ownerProfileId);
        int kolik = akounty.size();
        System.out.println("Pocet elementu v listu je : "+kolik);
        for (Account element: akounty) {
            System.out.println("Id accountu: "+element.getId());
            System.out.println("Balance accountu: "+element.getBalance());
        }
        return akounty;
        //return accountRepository.findByOwnerProfileId(ownerProfileId);
    }

    //getTransactionHistory
}
