package com.mois.transactionservice.repository;
import com.mois.transactionservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByOwnerProfileId(Long ownerProfileId);

}
