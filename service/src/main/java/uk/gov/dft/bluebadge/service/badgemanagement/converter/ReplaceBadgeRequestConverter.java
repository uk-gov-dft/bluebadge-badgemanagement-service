package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import java.time.LocalDate;

import uk.gov.dft.bluebadge.common.converter.ToEntityConverter;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeReplaceRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.ReplaceBadgeParams;

public class ReplaceBadgeRequestConverter implements ToEntityConverter<ReplaceBadgeParams, BadgeReplaceRequest> {

	@Override
	public ReplaceBadgeParams convertToEntity(BadgeReplaceRequest request) {
    return ReplaceBadgeParams.builder()
    		.badgeNumber(request.getBadgeNumber())
    		.deliveryCode(request.getDeliverToCode())
    		.deliveryOptionCode(request.getDeliveryOptionCode())
    		.reasonCode(request.getReplaceReasonCode())
    		.startDate(LocalDate.now())
    		.status(Status.REPLACED)
    		.build();
	}


}
