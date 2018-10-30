package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReplaceBadgeParams {

	private final String badgeNumber;
	private final BadgeEntity.Status status;
	private final String reasonCode;
	private final String deliveryCode;
	private final String deliveryOptionCode;
	private final LocalDate startDate;
	
}
