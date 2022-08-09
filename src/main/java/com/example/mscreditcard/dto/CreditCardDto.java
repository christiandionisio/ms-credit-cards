package com.example.mscreditcard.dto;

import java.math.BigDecimal;
import lombok.Data;

/**
 * CreditCard Dto.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
@Data
public class CreditCardDto {
  private String creditCardId;
  private String creditCardNumber;
  private String expirationDate;
  private String cvv;
  private BigDecimal creditLimit;
  private BigDecimal remainingCredit;
  private String category;
  private String customerId;
}