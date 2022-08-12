package com.example.mscard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BalanceDebitCardDto {
  private BigDecimal balance;
}
