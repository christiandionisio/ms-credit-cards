package com.example.mscard.util;

import com.example.mscard.dto.CreditDto;
import com.example.mscard.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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
public class CardBusinessRulesUtil {

  @Value("${customer.service.uri}")
  public static String uriCustomerService;

  @Value("${credit.service.uri}")
  public static String uriCreditService;

  private CardBusinessRulesUtil() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Communication with ms-customer.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  public static Mono<CustomerDto> findCustomerById(String id) {
    return WebClient.create().get()
        .uri("http://localhost:8082/customers/" + id)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .retrieve()
        .bodyToMono(CustomerDto.class);
  }

  public static Flux<CreditDto> findCreditWithOverdueDebt(String idCustomer) {
    return WebClient.create().get()
        .uri("http://localhost:8085/credits/" + "creditWithOverdueDebt?" +
                "customerId=" + idCustomer + "&date=" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToFlux(CreditDto.class);
  }
}
