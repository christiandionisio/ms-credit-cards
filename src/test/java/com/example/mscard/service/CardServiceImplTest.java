package com.example.mscard.service;

import com.example.mscard.dto.CreditDto;
import com.example.mscard.dto.CustomerDto;
import com.example.mscard.error.CustomerHasCreditDebtException;
import com.example.mscard.error.CustomerHasDebtException;
import com.example.mscard.model.Card;
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

    @MockBean
    private CardBusinessRulesUtil cardBusinessRulesUtil;

    @Autowired
    private CardServiceImpl service;

    @Test
    void findCreditCardByCustomerIdAndHasDebt() {
        Mockito.when(repository.findCardByCustomerIdAndHasDebt(Mockito.anyString(), Mockito.anyBoolean()))
            .thenReturn(Flux.empty());

        StepVerifier.create(service.findCreditCardByCustomerIdAndHasDebt("1", true))
            .expectComplete()
            .verify();
    }

    @Test
    void createTest() {

        Mockito.when(cardBusinessRulesUtil.findCustomerById(Mockito.anyString()))
            .thenReturn(Mono.just(getCustomerTest()));

        Mockito.when(repository.save(Mockito.any(Card.class)))
            .thenReturn(Mono.just(CardProvider.getCard()));

        Mockito.when(cardBusinessRulesUtil.findCreditWithOverdueDebt(Mockito.anyString()))
            .thenReturn(Flux.empty());

        Mockito.when(repository.findCardByCustomerIdAndHasDebt(Mockito.anyString(), Mockito.anyBoolean()))
            .thenReturn(Flux.empty());

        StepVerifier.create(service.create(CardProvider.getCard()))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void createTestWithCustomerHasCreditDebtExceptionTest() {

        Mockito.when(cardBusinessRulesUtil.findCustomerById(Mockito.anyString()))
                .thenReturn(Mono.just(getCustomerTest()));

        Mockito.when(repository.save(Mockito.any(Card.class)))
                .thenReturn(Mono.just(CardProvider.getCard()));

        Mockito.when(cardBusinessRulesUtil.findCreditWithOverdueDebt(Mockito.anyString()))
                .thenReturn(Flux.just(new CreditDto()));

        StepVerifier.create(service.create(CardProvider.getCard()))
                .expectError(CustomerHasCreditDebtException.class)
                .verify();
    }

    @Test
    void createTestWithCustomerHasDebtExceptionTest() {

        Mockito.when(cardBusinessRulesUtil.findCustomerById(Mockito.anyString()))
                .thenReturn(Mono.just(getCustomerTest()));

        Mockito.when(repository.save(Mockito.any(Card.class)))
                .thenReturn(Mono.just(CardProvider.getCard()));

        Mockito.when(cardBusinessRulesUtil.findCreditWithOverdueDebt(Mockito.anyString()))
                .thenReturn(Flux.empty());

        Mockito.when(repository.findCardByCustomerIdAndHasDebt(Mockito.anyString(), Mockito.anyBoolean()))
                .thenReturn(Flux.just(CardProvider.getCard()));

        StepVerifier.create(service.create(CardProvider.getCard()))
                .expectError(CustomerHasDebtException.class)
                .verify();
    }

    private CustomerDto getCustomerTest() {
        return new CustomerDto();
    }

}