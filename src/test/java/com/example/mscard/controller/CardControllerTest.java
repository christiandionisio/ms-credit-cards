package com.example.mscard.controller;

import com.example.mscard.dto.BalanceCreditCardDto;
import com.example.mscard.dto.CardDto;
import com.example.mscard.model.Card;
import com.example.mscard.provider.CardProvider;
import com.example.mscard.service.CardService;
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
public class CardControllerTest {

  @MockBean
  CardService creditCardService;

  @Autowired
  private WebTestClient webClient;

  @BeforeEach
  void setUp() {
  }

  @Test
  @DisplayName("Get all cards")
  void findAll() {
    Mockito.when(creditCardService.findAll())
            .thenReturn(Flux.fromIterable(CardProvider.getCardList()));

    webClient.get()
            .uri("/cards")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBodyList(Card.class)
            .consumeWith(response -> {
              List<Card> creditCardList = response.getResponseBody();
              creditCardList.forEach(c -> {
                System.out.println(c.getCardId());
              });
              Assertions.assertThat(creditCardList.size() > 0).isTrue();
            });
    //.hasSize(1);
  }

  @Test
  @DisplayName("Create Card")
  void create() {
    Mockito.when(creditCardService.create(Mockito.any(Card.class)))
            .thenReturn(Mono.just(CardProvider.getCard()));

    webClient.post().uri("/cards")
            .body(Mono.just(CardProvider.getCardDto()), CardDto.class)
            .exchange()
            .expectStatus().isNoContent();
  }

  @Test
  @DisplayName("Read Card")
  void read() {
    Mockito.when(creditCardService.findById(Mockito.anyString()))
            .thenReturn(Mono.just(CardProvider.getCard()));

    webClient.get().uri("/cards/1")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBody(Card.class)
            .consumeWith(response -> {
              Card creditCard = response.getResponseBody();
              Assertions.assertThat(creditCard.getCardId()).isEqualTo(CardProvider.getCard().getCardId());
            });
  }

  @Test
  @DisplayName("Update Card")
  void update() {
    Mockito.when(creditCardService.findById(Mockito.anyString()))
            .thenReturn(Mono.just(CardProvider.getCard()));
    Mockito.when(creditCardService.update(Mockito.any(Card.class)))
            .thenReturn(Mono.just(CardProvider.getCard()));

    webClient.put().uri("/cards/1")
            .body(Mono.just(CardProvider.getCardDto()), CardDto.class)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Card.class)
            .isEqualTo(CardProvider.getCard());
  }

  @Test
  @DisplayName("Delete Card")
  void delete() {
    Mockito.when(creditCardService.findById(Mockito.anyString()))
            .thenReturn(Mono.just(CardProvider.getCard()));
    Mockito.when(creditCardService.delete(Mockito.anyString()))
            .thenReturn(Mono.empty());

    webClient.delete().uri("/cards/1")
            .exchange()
            .expectStatus().isNoContent();
  }

  @Test
  @DisplayName("Get balance of a creditCard Product")
  void getBalanceAvailable() {
    Mockito.when(creditCardService.getAvailableBalance(Mockito.anyString()))
            .thenReturn(Mono.just(new BalanceCreditCardDto(BigDecimal.valueOf(5000), BigDecimal.valueOf(450.34))));

    webClient.get().uri("/cards/balance/1")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBody(BalanceCreditCardDto.class)
            .consumeWith(response -> {
              BalanceCreditCardDto balanceDto = response.getResponseBody();
              Assertions.assertThat(balanceDto.getCreditLimit()).isEqualTo(CardProvider.getCard().getCreditLimit());
            });
  }

  @Test
  @DisplayName("Get cards of a Customer")
  void findByCustomerId() {
    Mockito.when(creditCardService.findByCustomerId(Mockito.anyString()))
            .thenReturn(Flux.fromIterable(CardProvider.getCardList()));

    webClient.get().uri("/cards/findByCustomerId/1")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBodyList(Card.class)
            .consumeWith(response -> {
              List<Card> creditCardList = response.getResponseBody();
              creditCardList.forEach(c -> {
                System.out.println(c.getCardId());
              });
              Assertions.assertThat(creditCardList.size() > 0).isTrue();
            });
  }

  @Test
  @DisplayName("Get creditCards of a Customer")
  void findCreditCardsWithOverdueDebtTest() {
    Mockito.when(creditCardService.findCreditCardByCustomerIdAndHasDebt(Mockito.anyString(), Mockito.anyBoolean()))
            .thenReturn(Flux.fromIterable(CardProvider.getCardList()));

    webClient.get().uri("/cards/creditCardsWithOverdueDebt?customerId=" +
                    "62e8bb6cfaf5b87c8030047c&hasDebt=true")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Card.class)
            .hasSize(1);
  }
}
