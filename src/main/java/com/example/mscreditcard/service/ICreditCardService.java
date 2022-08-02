package com.example.mscreditcard.service;

import com.example.mscreditcard.dto.BalanceDto;
import com.example.mscreditcard.model.CreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICreditCardService {
    Flux<CreditCard> findAll();
    Mono<CreditCard> create(CreditCard creditCard);
    Mono<CreditCard> update(CreditCard creditCard);
    Mono<Void> delete(String creditCardId);

    Mono<CreditCard> findById(String id);

    Mono<BalanceDto> getAvailableBalance(String creditCardId);

    Mono<Long> countCreditCardsByCustomerId(String customerId);
}
