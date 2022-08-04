package com.example.mscreditcard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseTemplateDto {

  private Object response;
  private String errorMessage;

}
