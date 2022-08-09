package com.example.mscreditcard.provider;

import com.example.mscreditcard.dto.CreditCardDto;
import com.example.mscreditcard.enums.CategoryEnum;
import com.example.mscreditcard.model.CreditCard;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreditCardProvider {

  public static final String CREDIT_CARD_NUMBER = "1234567890123456";

  public static List<CreditCard> getCreditCardList() {
    List<CreditCard> creditList = new ArrayList<>();
    creditList.add(getCreditCard());
    return creditList;
  }

  public static CreditCard getCreditCard() {
    CreditCard creditCard = new CreditCard();
    creditCard.setCreditCardId("1");
    creditCard.setCreditCardNumber(CREDIT_CARD_NUMBER);
    creditCard.setExpirationDate("07/29");
    creditCard.setCvv("123");
    creditCard.setCreditLimit(BigDecimal.valueOf(5000));
    creditCard.setRemainingCredit(BigDecimal.valueOf(450.23));
    creditCard.setCategory(CategoryEnum.PLATINUM.getValue());
    creditCard.setCustomerId("1");
    return creditCard;
  }

  public static CreditCardDto getCreditCardDto() {
    return CreditCardDto.builder()
            .creditCardId("1")
            .creditCardNumber(CREDIT_CARD_NUMBER)
            .expirationDate("07/29")
            .cvv("123")
            .creditLimit(BigDecimal.valueOf(5000))
            .remainingCredit(BigDecimal.valueOf(450.23))
            .category(CategoryEnum.PLATINUM.getValue())
            .customerId("1")
            .build();
  }
}
