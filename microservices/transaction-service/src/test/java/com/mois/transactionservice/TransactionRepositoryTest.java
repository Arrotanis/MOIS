package com.mois.transactionservice;

import com.mois.transactionservice.model.Account;
import com.mois.transactionservice.model.Transaction;
import com.mois.transactionservice.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.UUID;

@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private AccountRepository accountRepository;
    @Test
    public void transactionRepositoryTest(){
        Transaction transaction = new Transaction();
        transaction.setTransactionNumber(UUID.randomUUID().toString());
        transaction.setDescription("popisek");
        transaction.setTransactionAmount(10100);
        Account testAccount = new Account();
        testAccount.setBalance(100);
        testAccount.setOwnerProfileId((long)1);
        Account testAccount2 = new Account();
        testAccount2.setBalance(100);
        testAccount2.setOwnerProfileId((long)1);
        testEntityManager.persistAndFlush(testAccount);
        testEntityManager.persistAndFlush(testAccount2);

        Optional<Account> optionalSourceAccount = accountRepository.findById((long)1);
        Optional<Account> optionalTargetAccount = accountRepository.findById((long)2);
        Account sourceAccount = optionalSourceAccount.get();
        Account targetAccount = optionalTargetAccount.get();


        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);

        testEntityManager.persistAndFlush(transaction);

    }
}
