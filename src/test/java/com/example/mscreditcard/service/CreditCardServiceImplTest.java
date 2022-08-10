package com.example.mscreditcard.service;

import com.example.mscreditcard.repo.CreditCardRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class CreditCardServiceImplTest {

    @MockBean
    private CreditCardRepository repository;

    @Autowired
    private CreditCardServiceImpl service;

    @Test
    void findCreditCardByCustomerIdAndHasDebt() {
        Mockito.when(repository.findCreditCardByCustomerIdAndHasDebt(Mockito.anyString(), Mockito.anyBoolean()))
            .thenReturn(Flux.empty());

        StepVerifier.create(service.findCreditCardByCustomerIdAndHasDebt("1", true))
            .expectComplete()
            .verify();
    }

}