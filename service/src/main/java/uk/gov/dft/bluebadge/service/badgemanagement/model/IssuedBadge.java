package uk.gov.dft.bluebadge.service.badgemanagement.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IssuedBadge {
  private final String laShortCode;
  private final String badgeNumber;
  private final LocalDateTime issuedTimestamp;
}
