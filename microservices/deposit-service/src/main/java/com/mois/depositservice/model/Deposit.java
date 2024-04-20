package com.mois.depositservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="t_deposits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int depositedBalance;
    private Long linkedAccountId;

    private Long ownerProfileId;

    private float interestRate;

    //startDate
    private LocalDateTime startDate;

    //endDate
    private LocalDateTime endDate;
}
