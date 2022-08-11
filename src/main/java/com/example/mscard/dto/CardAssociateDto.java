package com.example.mscard.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CardAssociateDto {
  private String customerId;
  private String cardId;
  private String mainAccountId;
  private List<String> accountIdList;
}
