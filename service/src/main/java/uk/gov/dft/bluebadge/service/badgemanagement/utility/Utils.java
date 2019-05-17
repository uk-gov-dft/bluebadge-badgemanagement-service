package uk.gov.dft.bluebadge.service.badgemanagement.utility;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import uk.gov.dft.bluebadge.common.service.enums.EligibilityType;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utils {

  public static boolean isAutomaticEligibility(EligibilityType code) {
    return code != null
        && (code.equals(EligibilityType.PIP)
        || code.equals(EligibilityType.DLA)
        || code.equals(EligibilityType.BLIND)
        || code.equals(EligibilityType.AFRFCS)
        || code.equals(EligibilityType.WPMS));
  }
}
