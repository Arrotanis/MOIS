package com.mois.transactionservice.repository;
import com.mois.transactionservice.model.Account;
import com.mois.transactionservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySourceAccount(Account ownerProfileId1);
}
