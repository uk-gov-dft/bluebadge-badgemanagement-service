package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateBadgeStatusParams {
  private String badgeNumber;
  private BadgeEntity.Status toStatus;
  private BadgeEntity.Status fromStatus;
}
