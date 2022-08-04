package com.example.mscreditcard.util;

import com.example.mscreditcard.dto.CustomerDto;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


public class CreditCardBusinessRulesUtil {

  public static Mono<CustomerDto> findCustomerById(String id) {
    return WebClient.create().get()
        .uri("http://localhost:9082/customers/" + id)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(CustomerDto.class);
  }

}
