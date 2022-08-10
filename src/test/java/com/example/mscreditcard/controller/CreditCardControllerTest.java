package com.example.mscreditcard.controller;

import com.example.mscreditcard.dto.BalanceDto;
import com.example.mscreditcard.dto.CreditCardDto;
import com.example.mscreditcard.model.CreditCard;
import com.example.mscreditcard.provider.CreditCardProvider;
import com.example.mscreditcard.service.CreditCardService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreditCardControllerTest {

  @MockBean
  CreditCardService creditCardService;

  @Autowired
  private WebTestClient webClient;

  @BeforeEach
  void setUp() {
  }

  @Test
  @DisplayName("Get all creditCards")
  void findAll() {
    Mockito.when(creditCardService.findAll())
            .thenReturn(Flux.fromIterable(CreditCardProvider.getCreditCardList()));

    webClient.get()
            .uri("/credit-cards")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBodyList(CreditCard.class)
            .consumeWith(response -> {
              List<CreditCard> creditCardList = response.getResponseBody();
              creditCardList.forEach(c -> {
                System.out.println(c.getCreditCardId());
              });
              Assertions.assertThat(creditCardList.size() > 0).isTrue();
            });
    //.hasSize(1);
  }

  @Test
  @DisplayName("Create CreditCard")
  void create() {
    Mockito.when(creditCardService.create(Mockito.any(CreditCard.class)))
            .thenReturn(Mono.just(CreditCardProvider.getCreditCard()));

    webClient.post().uri("/credit-cards")
            .body(Mono.just(CreditCardProvider.getCreditCardDto()), CreditCardDto.class)
            .exchange()
            .expectStatus().isCreated();
  }

  @Test
  @DisplayName("Read creditCard")
  void read() {
    Mockito.when(creditCardService.findById(Mockito.anyString()))
            .thenReturn(Mono.just(CreditCardProvider.getCreditCard()));

    webClient.get().uri("/credit-cards/1")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBody(CreditCard.class)
            .consumeWith(response -> {
              CreditCard creditCard = response.getResponseBody();
              Assertions.assertThat(creditCard.getCreditCardId()).isEqualTo(CreditCardProvider.getCreditCard().getCreditCardId());
            });
  }

  @Test
  @DisplayName("Update creditCard")
  void update() {
    Mockito.when(creditCardService.findById(Mockito.anyString()))
            .thenReturn(Mono.just(CreditCardProvider.getCreditCard()));
    Mockito.when(creditCardService.update(Mockito.any(CreditCard.class)))
            .thenReturn(Mono.just(CreditCardProvider.getCreditCard()));

    webClient.put().uri("/credit-cards/1")
            .body(Mono.just(CreditCardProvider.getCreditCardDto()), CreditCardDto.class)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(CreditCard.class)
            .isEqualTo(CreditCardProvider.getCreditCard());
  }

  @Test
  @DisplayName("Delete creditCard")
  void delete() {
    Mockito.when(creditCardService.findById(Mockito.anyString()))
            .thenReturn(Mono.just(CreditCardProvider.getCreditCard()));
    Mockito.when(creditCardService.delete(Mockito.anyString()))
            .thenReturn(Mono.empty());

    webClient.delete().uri("/credit-cards/1")
            .exchange()
            .expectStatus().isNoContent();
  }

  @Test
  @DisplayName("Get balance of a creditCard Product")
  void getBalanceAvailable() {
    Mockito.when(creditCardService.getAvailableBalance(Mockito.anyString()))
            .thenReturn(Mono.just(new BalanceDto(BigDecimal.valueOf(5000), BigDecimal.valueOf(450.34))));

    webClient.get().uri("/credit-cards/balance/1")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBody(BalanceDto.class)
            .consumeWith(response -> {
              BalanceDto balanceDto = response.getResponseBody();
              Assertions.assertThat(balanceDto.getCreditLimit()).isEqualTo(CreditCardProvider.getCreditCard().getCreditLimit());
            });
  }

  @Test
  @DisplayName("Get creditCards of a Customer")
  void findByCustomerId() {
    Mockito.when(creditCardService.findByCustomerId(Mockito.anyString()))
            .thenReturn(Flux.fromIterable(CreditCardProvider.getCreditCardList()));

    webClient.get().uri("/credit-cards/findByCustomerId/1")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBodyList(CreditCard.class)
            .consumeWith(response -> {
              List<CreditCard> creditCardList = response.getResponseBody();
              creditCardList.forEach(c -> {
                System.out.println(c.getCreditCardId());
              });
              Assertions.assertThat(creditCardList.size() > 0).isTrue();
            });
  }

  @Test
  @DisplayName("Get creditCards of a Customer")
    void findCreditCardsWithOverdueDebtTest() {
      Mockito.when(creditCardService.findCreditCardByCustomerIdAndHasDebt(Mockito.anyString(), Mockito.anyBoolean()))
              .thenReturn(Flux.fromIterable(CreditCardProvider.getCreditCardList()));

      webClient.get().uri("/credit-cards/creditCardsWithOverdueDebt?customerId=" +
                      "62e8bb6cfaf5b87c8030047c&hasDebt=true")
              .exchange()
              .expectStatus().isOk()
              .expectBodyList(CreditCard.class)
              .hasSize(1);
  }
}
