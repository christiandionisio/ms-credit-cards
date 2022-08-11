package com.example.mscard.util;

import com.example.mscard.dto.CreditDto;
import com.example.mscard.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * Layer of communication with other services.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
@Component
public class CardBusinessRulesUtil {

  @Value("${customer.service.uri}")
  private String uriCustomerService;

  @Value("${credit.service.uri}")
  private String uriCreditService;

  private CardBusinessRulesUtil() {
  }

  /**
   * Communication with ms-customer.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  public Mono<CustomerDto> findCustomerById(String id) {
    System.out.println("Pintando la variable de entorno: " + uriCustomerService);
    return WebClient.create().get()
        .uri(uriCustomerService + id)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .retrieve()
        .bodyToMono(CustomerDto.class);
  }

  public Flux<CreditDto> findCreditWithOverdueDebt(String idCustomer) {
    return WebClient.create().get()
        .uri(uriCreditService + "creditWithOverdueDebt?" +
                "customerId=" + idCustomer + "&date=" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToFlux(CreditDto.class);
  }
}
