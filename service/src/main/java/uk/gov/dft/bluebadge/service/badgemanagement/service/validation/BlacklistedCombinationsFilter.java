package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BlacklistedCombinationsFilter {

  @Value(("${blacklisted.combinations}"))
  private String combinations;

  public List<String> getCombinations() {
    return Arrays.asList(combinations.split(","));
  }

  public boolean valid(String badgeNumber) {
    return !getCombinations()
        .stream()
        .filter(i -> badgeNumber.contains(i.toUpperCase()))
        .findFirst()
        .isPresent();
  }
}
