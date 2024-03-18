package com.mois.transactionservice.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="t_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int balance;
    @OneToMany(mappedBy = "sourceAccount")
    private List<Transaction> sourceTransactions;

    @OneToMany(mappedBy = "targetAccount")
    private List<Transaction> targetTransactions;

    private Long ownerProfileId;
}
