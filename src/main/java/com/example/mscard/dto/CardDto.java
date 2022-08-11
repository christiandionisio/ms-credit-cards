package com.example.mscard.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

/**
 * Card Dto.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
@Data
@Builder
public class CardDto {
  private String cardId;
  private String cardNumber;
  private String expirationDate;
  private String cvv;
  private BigDecimal creditLimit;
  private BigDecimal remainingCredit;
  private String category;
  private String customerId;
  private String cardType;
  private String mainAccountId;
  private boolean hasDebt;
}
