package com.example.mscard.controller;

import com.example.mscard.dto.CardAssociateDto;
import com.example.mscard.dto.CardDto;
import com.example.mscard.dto.ResponseTemplateDto;
import com.example.mscard.error.CustomerHasCreditDebtException;
import com.example.mscard.error.CustomerHasDebtException;
import com.example.mscard.model.Card;
import com.example.mscard.service.CardService;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * Controller layer of Card.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
@RestController
@RequestMapping("/cards")
public class CardController {

  @Autowired
  private CardService service;

  private static final Logger logger = LogManager.getLogger(CardController.class);

  private ModelMapper modelMapper = new ModelMapper();

  /**
   * Get list of Cards.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @GetMapping
  public Mono<ResponseEntity<Flux<Card>>> findAll() {
    return Mono.just(
            ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(service.findAll()));
  }

  /**
   * Save a Card(DEBIT O CREDIT).
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @PostMapping
  public Mono<ResponseEntity<Object>> create(@RequestBody CardDto cardDto) {
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    return service.create(modelMapper.map(cardDto, Card.class))
            .flatMap(c -> Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)))
            .onErrorResume(e -> {
              if (e instanceof CustomerHasDebtException
                      || e instanceof CustomerHasCreditDebtException) {
                logger.error(e.getMessage());
                return Mono.just(new ResponseEntity<>(new ResponseTemplateDto(null,
                        e.getMessage()), HttpStatus.FORBIDDEN));
              }
              logger.error(e.getMessage());
              return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
            })
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  /**
   * Get detail of a CARD(DEBIT OR CREDIT) by Id.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @GetMapping("/{id}")
  public Mono<ResponseEntity<Card>> read(@PathVariable String id) {
    return service.findById(id).map(card -> ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(card))
            .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**
   * Update Card By Id.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @PutMapping("/{id}")
  public Mono<ResponseEntity<Card>> update(@RequestBody CardDto cardDto,
                                           @PathVariable String id) {
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    return service.findById(id)
            .flatMap(c -> service.update(modelMapper.map(cardDto, Card.class)))
            .map(creditCardUpdated -> ResponseEntity
                    .created(URI.create("/cards/".concat(creditCardUpdated.getCardId())))
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(creditCardUpdated))
            .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**
   * Delete Card(CREDIT OR DEBIT) By Id.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @DeleteMapping("/{cardId}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable String cardId) {
    return service.findById(cardId)
            .flatMap(creditCard -> service.delete(creditCard.getCardId())
                    .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
            .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
  }

  /**
   * Get available balance of a CreditCard product.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @GetMapping("/balance/{creditCardId}")
  public Mono<ResponseEntity<Object>> getBalanceAvailable(@PathVariable String creditCardId) {
    return service.getAvailableBalance(creditCardId)
            .flatMap(balance -> {
              ResponseEntity<Object> response = ResponseEntity.ok().body(balance);
              return Mono.just(response);
            })
            .defaultIfEmpty(new ResponseEntity<>(new ResponseTemplateDto(HttpStatus.NOT_FOUND,
                    "Card not found"), HttpStatus.NOT_FOUND));
  }

  @GetMapping("/findByCustomerId/{customerId}")
  public Mono<ResponseEntity<Flux<Card>>> findByCustomerId(@PathVariable String customerId) {
    return Mono.just(ResponseEntity.ok(service.findByCustomerId(customerId)));
  }

  /**
   * Get credit cards with overdue debt by customer ID.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @GetMapping("/creditCardsWithOverdueDebt")
  public Mono<ResponseEntity<Flux<Card>>> findCreditCardsWithOverdueDebt(
          @RequestParam String customerId, @RequestParam Boolean hasDebt) {
    return Mono.just(
            ResponseEntity.ok(
                    service.findCreditCardByCustomerIdAndHasDebt(customerId, hasDebt)));
  }

  @GetMapping("/count/{customerId}")
  public Mono<Long> getQuantityOfCreditCardsByCustomer(@PathVariable String customerId) {
    return service.countCreditCardsByCustomerId(customerId);
  }

  /**
   * Associate a debit card with main account and secondaries accounts.
   *
   * @author Alisson Arteaga
   * @version 1.0
   */
  @PostMapping("/debit/associate")
  public Mono<ResponseEntity<Object>> associateDebitCard(
          @RequestBody CardAssociateDto cardAssociateDto) {
    return service.associateDebitCardWithAccounts(cardAssociateDto)
            .flatMap(c -> Mono.just(ResponseEntity.ok().build()))
            .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)));
  }
}
