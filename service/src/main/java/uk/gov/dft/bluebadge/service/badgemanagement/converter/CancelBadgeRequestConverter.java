package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import uk.gov.dft.bluebadge.common.converter.ToEntityConverter;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeCancelRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;

public class CancelBadgeRequestConverter implements ToEntityConverter<CancelBadgeParams, BadgeCancelRequest>{
  @Override
  public CancelBadgeParams convertToEntity(BadgeCancelRequest badgeCancelRequest) {
    return CancelBadgeParams.builder()
        .badgeNo(badgeCancelRequest.getBadgeNumber())
        .cancelReasonCode(badgeCancelRequest.getCancelReasonCode())
        .build();
  }
}
