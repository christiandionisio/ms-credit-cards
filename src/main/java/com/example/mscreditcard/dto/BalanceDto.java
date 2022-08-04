package com.example.mscreditcard.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Balance Dto.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class BalanceDto {

  private BigDecimal creditLimit;

  private BigDecimal remainingCredit;

}
