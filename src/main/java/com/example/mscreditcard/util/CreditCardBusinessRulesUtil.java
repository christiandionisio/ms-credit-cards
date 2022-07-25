package com.example.mscreditcard.util;

import com.example.mscreditcard.dto.CustomerDTO;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


public class CreditCardBusinessRulesUtil {

    public static Mono<CustomerDTO> findCustomerById(String id) {
        return WebClient.create().get()
                .uri("http://localhost:8082/customers/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CustomerDTO.class);
    }

}
