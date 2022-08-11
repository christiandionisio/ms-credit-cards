package com.example.mscard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
  private String accountId;
  private String accountNumber;
  private String accountType;
  private String state;
  private BigDecimal balance;
  private String currency;
  private String createdAt;
  private String updatedAt;
  private String customerOwnerType;
  private String customerOwnerId;
  private String cardId;
  private LocalDateTime cardIdAssociateDate;
}
