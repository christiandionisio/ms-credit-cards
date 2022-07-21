package com.example.mscreditcard.controller;

import com.example.mscreditcard.model.CreditCard;
import com.example.mscreditcard.service.ICreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/credit-cards")
public class CreditCardController {

    @Autowired
    private ICreditCardService service;

    @GetMapping
    public Flux<CreditCard> findAll() {
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

}
