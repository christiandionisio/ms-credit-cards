package com.example.mscreditcard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BalanceDto {
    private BigDecimal creditLimit;
    private BigDecimal remainingCredit;
}
