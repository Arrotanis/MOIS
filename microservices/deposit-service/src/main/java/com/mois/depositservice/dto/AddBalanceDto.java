package com.mois.depositservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddBalanceDto {


    private Long targetAccountId;
    private int addBalanceAmount;
}
