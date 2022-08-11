package com.example.mscard.service;

import com.example.mscard.dto.BalanceDto;
import com.example.mscard.error.CustomerHasCreditDebtException;
import com.example.mscard.error.CustomerHasDebtException;
import com.example.mscard.model.Card;
import com.example.mscard.repo.CardRepository;
import com.example.mscard.util.CardBusinessRulesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Service Layer implementation of CreditCard product.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
@Service
public class CardServiceImpl implements CardService {

  @Autowired
  private CardRepository repository;

  @Override
  public Flux<Card> findAll() {
    return repository.findAll();
  }

  @Override
  public Mono<Card> create(Card creditCard) {
    System.out.println("CreditCard received: " + creditCard);
    return CardBusinessRulesUtil.findCustomerById(creditCard.getCustomerId())
            .doOnNext(customer -> System.out.println("Customer id obtenido: " + customer.getCustomerId()))
            .flatMap(customer -> hasDebtInCreditByCustomerId(creditCard.getCustomerId()))
            .flatMap(noHasDebts -> saveIfCustomerNotHaveDebt(creditCard));
  }

  @Override
  public Mono<Card> update(Card creditCard) {
    return repository.save(creditCard);
  }

  @Override
  public Mono<Void> delete(String creditCardId) {
    return repository.deleteById(creditCardId);
  }

  @Override
  public Mono<Card> findById(String id) {
    return repository.findById(id);
  }

  @Override
  public Mono<BalanceDto> getAvailableBalance(String creditCardId) {
    return repository.findById(creditCardId)
        .flatMap(creditCard -> {
          BalanceDto balanceDto = new BalanceDto(creditCard.getCreditLimit(),
              creditCard.getRemainingCredit());
          return Mono.just(balanceDto);
        });
  }

  @Override
  public Mono<Long> countCreditCardsByCustomerId(String customerId) {
    return repository.countCreditCardsByCustomerId(customerId);
  }

  @Override
  public Flux<Card> findByCustomerId(String customerId) {
    return repository.findByCustomerId(customerId);
  }

  @Override
  public Flux<Card> findCreditCardByCustomerIdAndHasDebt(String customerId, Boolean hasDebt) {
    return repository.findCreditCardByCustomerIdAndHasDebt(customerId, hasDebt);
  }

  private Mono<Boolean> hasDebtInCreditByCustomerId(String customerId) {
    return CardBusinessRulesUtil.findCreditWithOverdueDebt(customerId)
        .hasElements()
            .flatMap(hasDebt -> Boolean.TRUE.equals(hasDebt)
                    ? Mono.error(new CustomerHasCreditDebtException())
                    : Mono.just(false));
  }

  private Mono<Card> saveIfCustomerNotHaveDebt(Card creditCard) {
    return repository.findCreditCardByCustomerIdAndHasDebt(creditCard.getCustomerId(), true)
            .hasElements()
            .flatMap(hasDebt -> Boolean.TRUE.equals((hasDebt))
                    ? Mono.error(new CustomerHasDebtException())
                    : repository.save(creditCard));
  }
}
