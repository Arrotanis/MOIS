package com.mois.depositservice.repository;

import com.mois.depositservice.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit, Long> {

    List<Deposit> findDepositByLinkedAccountId(Long accountId);
}
