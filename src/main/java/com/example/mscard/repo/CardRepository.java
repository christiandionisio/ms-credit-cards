package com.example.mscard.repo;

import com.example.mscard.model.Card;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Repository layer of CreditCard product.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
public interface CardRepository extends ReactiveMongoRepository<Card, String> {

  Mono<Long> countCardsByCustomerId(String customerId);

  Flux<Card> findByCustomerId(String customerId);

  Flux<Card> findCardByCustomerIdAndHasDebt(String customerId, boolean hasDebt);

  Mono<Card> findCardByCustomerIdAndCardIdAndCardType(String customerId, String cardDebitId, String cardType);

}
