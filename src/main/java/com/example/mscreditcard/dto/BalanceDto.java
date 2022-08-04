package com.example.mscreditcard.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BalanceDto {
  private BigDecimal creditLimit;
  private BigDecimal remainingCredit;
}
