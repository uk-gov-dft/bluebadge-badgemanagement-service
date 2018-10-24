package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteBadgeParams {
  String badgeNo;
  BadgeEntity.Status deleteStatus;
}
