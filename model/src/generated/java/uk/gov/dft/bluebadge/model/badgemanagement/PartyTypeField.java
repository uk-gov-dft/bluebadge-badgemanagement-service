package uk.gov.dft.bluebadge.model.badgemanagement;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets PartyTypeField
 */
public enum PartyTypeField {
  
  PERSON("Person"),
  
  ORGANISATION("Organisation");

  private String value;

  PartyTypeField(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static PartyTypeField fromValue(String text) {
    for (PartyTypeField b : PartyTypeField.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

