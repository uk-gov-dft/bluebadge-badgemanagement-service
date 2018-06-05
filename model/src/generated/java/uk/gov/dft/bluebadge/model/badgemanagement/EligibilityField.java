package uk.gov.dft.bluebadge.model.badgemanagement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.constraints.*;

/** Reason for badge */
public enum EligibilityField {
  PIP("PIP"),

  DLA("DLA"),

  ARMED_FORCES_COMPENSATION_SCHEME("Armed Forces Compensation scheme"),

  WAR_PENSIONERS_MOBILITY_SUPPLEMENT("War Pensioners' Mobility Supplement"),

  REGISTERED_BLIND("Registered blind"),

  WALKING_ABILITY("Walking ability"),

  DISABILITY_IN_BOTH_ARMS("Disability in both arms"),

  CHILD_UNDER_3_WITH_BULKY_MEDICAL_EQUIPMENT("Child under 3 with bulky medical equipment"),

  CHILD_UNDER_3_WHO_NEEDS_TO_BE_NEAR_A_VEHICLE("Child under 3 who needs to be near a vehicle"),

  TERMINAL_ILLNESS("Terminal illness");

  private String value;

  EligibilityField(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static EligibilityField fromValue(String text) {
    for (EligibilityField b : EligibilityField.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
