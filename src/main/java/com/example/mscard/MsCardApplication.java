package com.example.mscard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Card application.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
@EnableEurekaClient
@SpringBootApplication
public class MsCardApplication {

  public static void main(String[] args) {
    SpringApplication.run(MsCardApplication.class, args);
  }

}
