package com.mois.depositservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDepositDto {


    private Long linkedAccountId;
    private Long ownerProfileId;
    private int depositedBalance;

}
