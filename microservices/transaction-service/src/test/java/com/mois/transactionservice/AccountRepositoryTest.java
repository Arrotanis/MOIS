package com.mois.transactionservice;

import com.mois.transactionservice.model.Account;
import com.mois.transactionservice.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void TestAccountRepository(){
        System.out.println("Checkpoint 1");
        Account testAccount = new Account();
        testAccount.setBalance(100);
        testAccount.setOwnerProfileId((long)1);

        testEntityManager.persistAndFlush(testAccount);

        List<Account> foundAccounts = accountRepository.findByOwnerProfileId(testAccount.getOwnerProfileId());
        Account tempAccount = new Account();
        System.out.println("Checkpoint 2");
        for (Account element: foundAccounts) {
            if (element.getId().equals(testAccount.getId())){

                tempAccount = element;
            }
        }
        assertThat(testAccount.getBalance()).isEqualTo(tempAccount.getBalance());
        System.out.println("Checkpoint 3");
    }


}
