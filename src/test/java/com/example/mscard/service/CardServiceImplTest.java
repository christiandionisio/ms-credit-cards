package com.example.mscard.service;

import com.example.mscard.provider.CardProvider;
import com.example.mscard.repo.CardRepository;
import com.example.mscard.util.CardBusinessRulesUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class CardServiceImplTest {

  @MockBean
  private CardRepository repository;

  @Autowired
  private CardServiceImpl service;

  @MockBean
  private CardBusinessRulesUtil cardBusinessRulesUtil;

  @Test
  void findCreditCardByCustomerIdAndHasDebt() {
    Mockito.when(repository.findCardByCustomerIdAndHasDebt(Mockito.anyString(), Mockito.anyBoolean()))
            .thenReturn(Flux.empty());

    StepVerifier.create(service.findCreditCardByCustomerIdAndHasDebt("1", true))
            .expectComplete()
            .verify();
  }

  @Test
  void countCreditCardsByCustomerId() {
    Mockito.when(repository.countCardsByCustomerId(Mockito.anyString()))
            .thenReturn(Mono.just(Long.valueOf(1)));

    StepVerifier.create(service.countCreditCardsByCustomerId("1"))
            .expectNext(Long.valueOf(1))
            .verifyComplete();
  }

  @Test
  void associateDebitCardWithAccounts() {
    Mockito.when(repository.findCardByCustomerIdAndCardIdAndCardType(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Mono.just(CardProvider.getCard()));

    Mockito.when(cardBusinessRulesUtil.findAccountByCustomerIdAndAccountId(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Mono.just(CardProvider.getAccountDto()));

    Mockito.when(cardBusinessRulesUtil.updateAccount(Mockito.any()))
            .thenReturn(Mono.just(CardProvider.getAccountDto()));

    Mockito.when(repository.save(Mockito.any()))
            .thenReturn(Mono.just(CardProvider.getCard()));

    StepVerifier.create(service.associateDebitCardWithAccounts(CardProvider.getCardAssociateDto()))
            .expectComplete()
            .verify();
  }

  @Test
  void findByCustomerIdAndDebitCardId() {
    Mockito.when(repository.findCardByCustomerIdAndCardIdAndCardType(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Mono.just(CardProvider.getCard()));

    StepVerifier.create(service.findByCustomerIdAndDebitCardId("1", "2"))
            .expectNext(CardProvider.getCard())
            .verifyComplete();
  }

}