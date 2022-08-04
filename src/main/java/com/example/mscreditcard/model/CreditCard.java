package com.example.mscreditcard.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "credit_cards")
@Data
@AllArgsConstructor
public class CreditCard {
  @Id
  private String creditCardId;
  private String creditCardNumber;
  private String expirationDate;
  private String cvv;
  private BigDecimal creditLimit;
  private BigDecimal remainingCredit;
  private String category;
  private String customerId;
}
