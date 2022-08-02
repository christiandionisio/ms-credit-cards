package com.example.mscreditcard.service;

import com.example.mscreditcard.model.CreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICreditCardService {
    Flux<CreditCard> findAll();
    Mono<CreditCard> create(CreditCard creditCard);
    Mono<CreditCard> update(CreditCard creditCard);
    Mono<Void> delete(String creditCardId);

    Mono<CreditCard> findById(String id);
    Flux<CreditCard> findByCustomerId(String customerId);
}
