package com.mois.transactionservice.dto;

import com.mois.transactionservice.model.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    private String transactionNumber;
    private String description;
    private int transactionAmount;

    private Account targetAccount;
}
