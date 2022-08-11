package com.example.mscard.provider;

import com.example.mscard.dto.CardDto;
import com.example.mscard.enums.CategoryEnum;
import com.example.mscard.enums.TypeEnum;
import com.example.mscard.model.Card;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CardProvider {

  public static final String CREDIT_CARD_NUMBER = "1234567890123456";

  public static List<Card> getCardList() {
    List<Card> creditList = new ArrayList<>();
    creditList.add(getCard());
    return creditList;
  }

  public static Card getCard() {
    Card creditCard = new Card();
    creditCard.setCardId("1");
    creditCard.setCardNumber(CREDIT_CARD_NUMBER);
    creditCard.setExpirationDate("07/29");
    creditCard.setCvv("123");
    creditCard.setCreditLimit(BigDecimal.valueOf(5000));
    creditCard.setRemainingCredit(BigDecimal.valueOf(450.23));
    creditCard.setCategory(CategoryEnum.PLATINUM.getValue());
    creditCard.setCardType(TypeEnum.CREDIT_CARD.getValue());
    creditCard.setMainAccountId("2");
    creditCard.setCustomerId("1");
    return creditCard;
  }

  public static CardDto getCardDto() {
    return CardDto.builder()
            .cardId("1")
            .cardNumber(CREDIT_CARD_NUMBER)
            .expirationDate("07/29")
            .cvv("123")
            .creditLimit(BigDecimal.valueOf(5000))
            .remainingCredit(BigDecimal.valueOf(450.23))
            .category(CategoryEnum.PLATINUM.getValue())
            .cardType(TypeEnum.CREDIT_CARD.getValue())
            .mainAccountId("2")
            .customerId("1")
            .build();
  }
}
