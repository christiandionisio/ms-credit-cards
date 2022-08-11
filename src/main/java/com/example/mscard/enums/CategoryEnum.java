package com.example.mscard.enums;

/**
 * Categories of Cards.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
public enum CategoryEnum {
  GOLD("GOLD"),
  PLATINUM("PLATINUM"),
  BLACK("BLACK");
  private final String value;

  CategoryEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
