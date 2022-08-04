package com.example.mscreditcard.service;

import com.example.mscreditcard.dto.BalanceDto;
import com.example.mscreditcard.model.CreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Service Layer of CreditCard product.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
public interface CreditCardService {
  Flux<CreditCard> findAll();

  Mono<CreditCard> create(CreditCard creditCard);

  Mono<CreditCard> update(CreditCard creditCard);

  Mono<Void> delete(String creditCardId);

  Mono<CreditCard> findById(String id);

  Mono<BalanceDto> getAvailableBalance(String creditCardId);

  Mono<Long> countCreditCardsByCustomerId(String customerId);

  Flux<CreditCard> findByCustomerId(String customerId);
}
