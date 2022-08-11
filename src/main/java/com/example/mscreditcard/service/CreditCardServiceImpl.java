package com.example.mscreditcard.service;

import com.example.mscreditcard.dto.BalanceDto;
import com.example.mscreditcard.error.CustomerHasDebtException;
import com.example.mscreditcard.model.CreditCard;
import com.example.mscreditcard.repo.CreditCardRepository;
import com.example.mscreditcard.util.CreditCardBusinessRulesUtil;
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
public class CreditCardServiceImpl implements CreditCardService {

  @Autowired
  private CreditCardRepository repository;

  @Override
  public Flux<CreditCard> findAll() {
    return repository.findAll();
  }

  @Override
  public Mono<CreditCard> create(CreditCard creditCard) {
    return CreditCardBusinessRulesUtil.findCustomerById(creditCard.getCustomerId())
        .flatMap(customer -> saveIfCustomerNotHaveDebt(creditCard));
  }

  @Override
  public Mono<CreditCard> update(CreditCard creditCard) {
    return repository.save(creditCard);
  }

  @Override
  public Mono<Void> delete(String creditCardId) {
    return repository.deleteById(creditCardId);
  }

  @Override
  public Mono<CreditCard> findById(String id) {
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
  public Flux<CreditCard> findByCustomerId(String customerId) {
    return repository.findByCustomerId(customerId);
  }

  @Override
  public Flux<CreditCard> findCreditCardByCustomerIdAndHasDebt(String customerId, Boolean hasDebt) {
    return repository.findCreditCardByCustomerIdAndHasDebt(customerId, hasDebt);
  }

  private Mono<CreditCard> saveIfCustomerNotHaveDebt(CreditCard creditCard) {
    return repository.findCreditCardByCustomerIdAndHasDebt(creditCard.getCustomerId(), true)
            .hasElements()
            .flatMap(hasDebt -> Boolean.TRUE.equals((hasDebt))
                    ? Mono.error(new CustomerHasDebtException())
                    : repository.save(creditCard));
  }
}
