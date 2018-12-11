package uk.gov.dft.bluebadge.service.badgemanagement.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BatchType {
  STANDARD,
  FASTTRACK,
  LA;

  //  @JsonCreator
  public static BatchType fromValue(String text) {
    for (BatchType b : BatchType.values()) {
      if (String.valueOf(b.name()).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
