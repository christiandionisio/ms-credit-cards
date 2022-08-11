package com.example.mscard.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Credit Card document.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
@Document(collection = "cards")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Card {
  @Id
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
  private Boolean hasDebt;
}
