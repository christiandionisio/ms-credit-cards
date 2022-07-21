package com.example.mscreditcard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

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
    private String category;
    private String customerId;
}
