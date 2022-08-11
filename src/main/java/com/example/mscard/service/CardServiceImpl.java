package com.example.mscard.service;

import com.example.mscard.dto.AccountDto;
import com.example.mscard.dto.BalanceDto;
import com.example.mscard.dto.CardAssociateDto;
import com.example.mscard.enums.TypeEnum;
import com.example.mscard.error.CustomerHasCreditDebtException;
import com.example.mscard.error.CustomerHasDebtException;
import com.example.mscard.model.Card;
import com.example.mscard.repo.CardRepository;
import com.example.mscard.util.CardBusinessRulesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


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

  @Autowired
  private CardBusinessRulesUtil cardBusinessRulesUtil;

  @Override
  public Flux<Card> findAll() {
    return repository.findAll();
  }

  @Override
  public Mono<Card> create(Card creditCard) {
    return cardBusinessRulesUtil.findCustomerById(creditCard.getCustomerId())
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
    return cardBusinessRulesUtil.findCreditWithOverdueDebt(customerId)
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

  @Override
  public Mono<Void> associateDebitCardWithAccounts(CardAssociateDto cardAssociateDto) {
    return repository.findCardByCustomerIdAndCardIdAndCardType(cardAssociateDto.getCustomerId(),
                    cardAssociateDto.getCardId(), TypeEnum.DEBIT_CARD.getValue())
            .doOnNext(card -> System.out.println("DebitCard : " + card))
            .flatMap(card -> {
              card.setMainAccountId(cardAssociateDto.getMainAccountId());
              return findAndUpdateAccount(cardAssociateDto.getCustomerId(), cardAssociateDto.getMainAccountId(), card.getCardId())
                      .flatMap(a -> repository.save(card)
                              .flatMap(c -> {
                                if(cardAssociateDto.getAccountIdList() == null){
                                  return Mono.empty();
                                }else{
                                  return Flux.fromIterable(cardAssociateDto.getAccountIdList())
                                          .flatMap(accountId -> findAndUpdateAccount(cardAssociateDto.getCustomerId(), accountId, card.getCardId())).then(Mono.empty());
                                }
                              }));
            });
  }

  public Mono<AccountDto> findAndUpdateAccount(String customerId, String accountId, String cardId) {
    return cardBusinessRulesUtil.findAccountByCustomerIdAndAccountId(customerId, accountId)
            .flatMap(accountDto -> {
              accountDto.setCardId(cardId);
              accountDto.setCardIdAssociateDate(LocalDateTime.now());
              System.out.println("Account updated: " + accountDto);
              return cardBusinessRulesUtil.updateAccount(accountDto);
            });
  }
}
