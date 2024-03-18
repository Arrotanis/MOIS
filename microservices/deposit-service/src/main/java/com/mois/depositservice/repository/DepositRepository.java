package com.mois.depositservice.repository;

import com.mois.depositservice.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
}
