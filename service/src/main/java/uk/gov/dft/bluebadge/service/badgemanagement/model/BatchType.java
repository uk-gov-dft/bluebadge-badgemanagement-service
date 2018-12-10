package uk.gov.dft.bluebadge.service.badgemanagement.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BatchType {
  STANDARD("STANDARD"),
  FASTTRACK("FASTTRACK"),
  LA("LA");

  private String value;

  BatchType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  //  @JsonCreator
  public static BatchType fromValue(String text) {
    for (BatchType b : BatchType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
