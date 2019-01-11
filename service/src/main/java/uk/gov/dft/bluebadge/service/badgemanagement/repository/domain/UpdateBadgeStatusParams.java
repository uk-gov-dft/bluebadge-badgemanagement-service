package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateBadgeStatusParams {
  String badgeNumber;
  BadgeEntity.Status toStatus;
  BadgeEntity.Status fromStatus;
}
