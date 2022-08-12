package com.example.mscard.service;

import com.example.mscard.dto.AccountDto;
import com.example.mscard.dto.BalanceCreditCardDto;
import com.example.mscard.dto.BalanceDebitCardDto;
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
  public Mono<Card> create(Card card) {
    return cardBusinessRulesUtil.findCustomerById(card.getCustomerId())
            .flatMap(customer -> hasDebtInCreditByCustomerId(card.getCustomerId()))
            .flatMap(noHasDebts -> saveIfCustomerNotHaveDebt(card));
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
  public Mono<Object> getAvailableBalance(String cardId) {
    return repository.findById(cardId)
            .flatMap(card -> {
              //if is creditCard
              if(card.getCardType().equals(TypeEnum.CREDIT_CARD.getValue())){
                BalanceCreditCardDto balanceDto = new BalanceCreditCardDto(card.getCreditLimit(),
                        card.getRemainingCredit());
                return Mono.just(balanceDto);
              }else{
                //if is debitCard
                String mainAccountId = card.getMainAccountId();
                return cardBusinessRulesUtil.findAccountByCustomerIdAndAccountId(card.getCustomerId(), mainAccountId)
                        .flatMap(accountDto -> {
                          BalanceDebitCardDto balanceDto = new BalanceDebitCardDto(accountDto.getBalance());
                          return Mono.just(balanceDto);
                        });
              }
            });
  }

  @Override
  public Mono<Long> countCreditCardsByCustomerId(String customerId) {
    return repository.countCardsByCustomerId(customerId);
  }

  @Override
  public Flux<Card> findByCustomerId(String customerId) {
    return repository.findByCustomerId(customerId);
  }

  @Override
  public Flux<Card> findCreditCardByCustomerIdAndHasDebt(String customerId, Boolean hasDebt) {
    return repository.findCardByCustomerIdAndHasDebt(customerId, hasDebt);
  }

  private Mono<Boolean> hasDebtInCreditByCustomerId(String customerId) {
    return cardBusinessRulesUtil.findCreditWithOverdueDebt(customerId)
            .hasElements()
            .flatMap(hasDebt -> Boolean.TRUE.equals(hasDebt)
                    ? Mono.error(new CustomerHasCreditDebtException())
                    : Mono.just(false));
  }

  private Mono<Card> saveIfCustomerNotHaveDebt(Card creditCard) {
    return repository.findCardByCustomerIdAndHasDebt(creditCard.getCustomerId(), true)
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

  @Override
  public Mono<Card> findByCustomerIdAndDebitCardId(String customerId, String debitCardId) {
    return repository.findCardByCustomerIdAndCardIdAndCardType(customerId, debitCardId, TypeEnum.DEBIT_CARD.getValue());
  }
}
