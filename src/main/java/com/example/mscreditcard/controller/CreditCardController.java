package com.example.mscreditcard.controller;

import com.example.mscreditcard.dto.ResponseTemplateDto;
import com.example.mscreditcard.model.CreditCard;
import com.example.mscreditcard.service.CreditCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/credit-cards")
public class CreditCardController {

  @Autowired
  private CreditCardService service;

  private static final Logger logger = LogManager.getLogger(CreditCardController.class);

  @GetMapping
  public Flux<CreditCard> findAll() {
    logger.debug("Debugging log");
    logger.info("Info log");
    logger.warn("Hey, This is a warning!");
    logger.error("Oops! We have an Error. OK");
    logger.fatal("Damn! Fatal error. Please fix me.");
    return service.findAll();
  }

  @PostMapping
  public Mono<ResponseEntity<CreditCard>> create(@RequestBody CreditCard creditCard) {
    return service.create(creditCard)
      .flatMap(c -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(c)))
      .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @PutMapping
  public Mono<CreditCard> update(@RequestBody CreditCard creditCard) {
    return service.update(creditCard);
  }

  @DeleteMapping("/{creditCardId}")
  public Mono<Void> delete(@PathVariable String creditCardId) {
    return service.delete(creditCardId);
  }

  @GetMapping("/{id}")
  public Mono<CreditCard> read(@PathVariable String id) {
    Mono<CreditCard> account = service.findById(id);
    return account;
  }

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

}
