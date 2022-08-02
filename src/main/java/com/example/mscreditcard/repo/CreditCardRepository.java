package com.example.mscreditcard.repo;

import com.example.mscreditcard.model.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {

    Mono<Long> countCreditCardsByCustomerId(String customerId);

    Flux<CreditCard> findByCustomerId(String customerId);
}
