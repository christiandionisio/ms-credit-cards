package com.example.mscreditcard.controller;

import com.example.mscreditcard.model.CreditCard;
import com.example.mscreditcard.service.ICreditCardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/credit-cards")
public class CreditCardController {

    @Autowired
    private ICreditCardService service;

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
    public Mono<CreditCard> create(@RequestBody CreditCard creditCard) {
        return service.create(creditCard);
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
    public Mono<CreditCard> read(@PathVariable String id){
        Mono<CreditCard> account = service.findById(id);
        return account;
    }

}
