package com.mois.transactionservice.service;

import com.mois.transactionservice.dto.TransactionDto;
import com.mois.transactionservice.model.Account;
import com.mois.transactionservice.model.Transaction;
import com.mois.transactionservice.repository.AccountRepository;
import com.mois.transactionservice.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
