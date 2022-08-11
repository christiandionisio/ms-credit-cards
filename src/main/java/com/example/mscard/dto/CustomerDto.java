package com.example.mscard.dto;

import java.util.Date;
import lombok.Data;


/**
 * Customer Dto.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
@Data
public class CustomerDto {
  private String customerId;
  private String name;
  private String lastName;
  private String email;
  private String documentType;
  private String documentNumber;
  private Date birthDate;
  private String customerType;
}
