package com.example.mscreditcard.controller;

import com.example.mscreditcard.dto.CreditCardDto;
import com.example.mscreditcard.dto.ResponseTemplateDto;
import com.example.mscreditcard.error.CustomerHasDebtException;
import com.example.mscreditcard.model.CreditCard;
import com.example.mscreditcard.service.CreditCardService;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller layer of CreditCard.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
@RestController
@RequestMapping("/credit-cards")
public class CreditCardController {

  @Autowired
  private CreditCardService service;

  private static final Logger logger = LogManager.getLogger(CreditCardController.class);

  private ModelMapper modelMapper = new ModelMapper();

  /**
   * Get list of CreditCard.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @GetMapping
  public Mono<ResponseEntity<Flux<CreditCard>>> findAll() {
    return Mono.just(
            ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(service.findAll()));
  }

  /**
   * Save a CreditCard.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @PostMapping
  public Mono<ResponseEntity<Object>> create(@RequestBody CreditCardDto creditCardDto) {
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    return service.create(modelMapper.map(creditCardDto, CreditCard.class))
            .flatMap(c -> Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)))
            .onErrorResume(e -> {
              if (e instanceof CustomerHasDebtException) {
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
   * Get detail of a creditCard by Id.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @GetMapping("/{id}")
  public Mono<ResponseEntity<CreditCard>> read(@PathVariable String id) {
    return service.findById(id).map(creditCard -> ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(creditCard))
            .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**
   * Update CreditCard By Id.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @PutMapping("/{id}")
  public Mono<ResponseEntity<CreditCard>> update(@RequestBody CreditCardDto creditCardDto,
                                             @PathVariable String id) {
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    return service.findById(id)
            .flatMap(c -> service.update(modelMapper.map(creditCardDto, CreditCard.class)))
            .map(creditCardUpdated -> ResponseEntity
                    .created(URI.create("/credit-cards/"
                            .concat(creditCardUpdated.getCreditCardId())))
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(creditCardUpdated))
            .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**
   * Delete CreditCard By Id.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @DeleteMapping("/{creditCardId}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable String creditCardId) {
    return service.findById(creditCardId)
            .flatMap(creditCard -> service.delete(creditCard.getCreditCardId())
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
        "Credit Card not found"), HttpStatus.NOT_FOUND));
  }

  @GetMapping("/findByCustomerId/{customerId}")
  public Mono<ResponseEntity<Flux<CreditCard>>> findByCustomerId(@PathVariable String customerId) {
    return Mono.just(ResponseEntity.ok(service.findByCustomerId(customerId)));
  }

  /**
   * Get credit cards with overdue debt by customer ID.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @GetMapping("/creditCardsWithOverdueDebt")
    public Mono<ResponseEntity<Flux<CreditCard>>> findCreditCardsWithOverdueDebt(
            @RequestParam String customerId, @RequestParam Boolean hasDebt) {
        return Mono.just(
                ResponseEntity.ok(
                        service.findCreditCardByCustomerIdAndHasDebt(customerId, hasDebt)));
    }
}
