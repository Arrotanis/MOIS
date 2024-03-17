package com.mois.transactionservice.service;

import com.mois.transactionservice.dto.AccountRequest;
import com.mois.transactionservice.dto.TransactionDto;
import com.mois.transactionservice.model.Account;
import com.mois.transactionservice.model.Transaction;
import com.mois.transactionservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
   /* public void createAccount(AccountRequest accountRequest) {
        Account account = new Account();

        List<Transaction> transactionList = accountRequest.getTransactionDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        account.setTransactionList(transactionList);
        accountRepository.save(account);
    }*/

    public void createAccount() {
        Account account = new Account();
        accountRepository.save(account);
    }

    private Transaction mapToDto(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setTransactionNumber(transactionDto.getTransactionNumber());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setTargetAccount(transactionDto.getTargetAccount());
        transaction.setTargetAccount(transactionDto.getTargetAccount());
        return transaction;
    }
}
