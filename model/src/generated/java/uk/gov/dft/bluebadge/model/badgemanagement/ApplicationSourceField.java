package uk.gov.dft.bluebadge.model.badgemanagement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.*;

/** Gets or Sets ApplicationSourceField */
public enum ApplicationSourceField {
  ONLINE("Online"),

  PAPER("Paper"),

  PHONE("Phone"),

  IN_PERSON("In person");

  private String value;

  ApplicationSourceField(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ApplicationSourceField fromValue(String text) {
    for (ApplicationSourceField b : ApplicationSourceField.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
