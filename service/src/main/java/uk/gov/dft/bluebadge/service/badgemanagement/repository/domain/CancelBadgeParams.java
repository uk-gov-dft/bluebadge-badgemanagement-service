package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.service.badgemanagement.model.CancelReason;

@Data
@Builder
public class CancelBadgeParams {
  String badgeNo;
  CancelReason cancelReasonCode;
  String localAuthorityShortCode;
}
