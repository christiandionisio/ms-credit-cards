package com.example.mscreditcard.repo;

import com.example.mscreditcard.model.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Repository layer of CreditCard product.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {

  Mono<Long> countCreditCardsByCustomerId(String customerId);

  Flux<CreditCard> findByCustomerId(String customerId);
}
