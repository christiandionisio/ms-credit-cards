package com.example.mscreditcard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "credit_cards")
@Data
@AllArgsConstructor
public class CreditCard {
    private String creditCardId;
    private String creditCardNumber;
    private String expirationDate;
    private String cvv;
    private String creditLimit;
    private String category;
    private String customerId;
}
