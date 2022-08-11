package com.example.mscard.enums;

/**
 * Type of Cards.
 *
 * @author Alisson Arteaga
 * @version 1.0
 */
public enum TypeEnum {
  CREDIT_CARD("CREDIT_CARD"),
  DEBIT_CARD("DEBIT_CARD");
  private final String value;

  TypeEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
