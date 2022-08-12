package com.example.mscard.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CardAssociateDto {
  private String customerId;
  private String cardId;
  private String mainAccountId;
  private List<String> accountIdList;
}
