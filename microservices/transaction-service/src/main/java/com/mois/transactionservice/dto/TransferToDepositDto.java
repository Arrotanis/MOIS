package com.mois.transactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferToDepositDto {

    private Long accountId;
    private int amountToTransfer;

}
