package com.example.mscard.service;

import com.example.mscard.dto.BalanceDto;
import com.example.mscard.dto.CardAssociateDto;
import com.example.mscard.model.Card;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Service Layer of CreditCard product.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
public interface CardService {
  Flux<Card> findAll();

  Mono<Card> create(Card creditCard);

  Mono<Card> update(Card creditCard);

  Mono<Void> delete(String creditCardId);

  Mono<Card> findById(String id);

  Mono<BalanceDto> getAvailableBalance(String creditCardId);

  Mono<Long> countCreditCardsByCustomerId(String customerId);

  Flux<Card> findByCustomerId(String customerId);

  Flux<Card> findCreditCardByCustomerIdAndHasDebt(String customerId, Boolean hasDebt);

  Mono<Void> associateDebitCardWithAccounts(CardAssociateDto cardAssociateDto);

}
