package com.example.mscreditcard.service;

import com.example.mscreditcard.dto.BalanceDto;
import com.example.mscreditcard.model.CreditCard;
import com.example.mscreditcard.repo.CreditCardRepository;
import com.example.mscreditcard.util.CreditCardBusinessRulesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditCardServiceImpl implements ICreditCardService {

    @Autowired
    private CreditCardRepository repository;

    @Override
    public Flux<CreditCard> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<CreditCard> create(CreditCard creditCard) {
        return CreditCardBusinessRulesUtil.findCustomerById(creditCard.getCustomerId())
                .flatMap(customer -> repository.save(creditCard));
    }

    @Override
    public Mono<CreditCard> update(CreditCard creditCard) {
        return repository.save(creditCard);
    }

    @Override
    public Mono<Void> delete(String creditCardId) {
        return repository.deleteById(creditCardId);
    }

    @Override
    public Mono<CreditCard> findById(String id) {
        return repository.findById(id);
    }
    @Override
    public Mono<BalanceDto> getAvailableBalance(String creditCardId) {
        return repository.findById(creditCardId)
                .flatMap(creditCard -> {
                    BalanceDto balanceDto = new BalanceDto(creditCard.getCreditLimit(), creditCard.getRemainingCredit());
                    Mono<BalanceDto> balanceDtoMono = Mono.just(balanceDto);
                    return balanceDtoMono;
                });
    }

    @Override
    public Mono<Long> countCreditCardsByCustomerId(String customerId) {
        return repository.countCreditCardsByCustomerId(customerId);
    }
}
