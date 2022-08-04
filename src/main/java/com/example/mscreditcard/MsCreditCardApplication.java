package com.example.mscreditcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * CreditCard application.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
@EnableEurekaClient
@SpringBootApplication
public class MsCreditCardApplication {

  public static void main(String[] args) {
    SpringApplication.run(MsCreditCardApplication.class, args);
  }

}
