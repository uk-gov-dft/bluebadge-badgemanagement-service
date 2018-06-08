package uk.gov.dft.bluebadge.model.badgemanagement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.*;

/** The current status of the blue bage */
public enum StatusField {
  ACTIVE("Active");

  private String value;

  StatusField(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StatusField fromValue(String text) {
    for (StatusField b : StatusField.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
