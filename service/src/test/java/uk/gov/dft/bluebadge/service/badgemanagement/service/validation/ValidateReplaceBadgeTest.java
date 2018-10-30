package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import java.time.LocalDate;

import org.junit.Test;

import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestBase;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.ReplaceBadgeParams;

public class ValidateReplaceBadgeTest extends BadgeTestBase  {

	private ValidateReplaceBadge validator = new ValidateReplaceBadge(referenceDataService);

	@Test
  public void validate_params_ok() {
	  	ReplaceBadgeParams params = ReplaceBadgeParams.builder()
	 																									.badgeNumber("72B9HA")
	 																									.deliveryCode("HOME")
	 																									.deliveryOptionCode("FAST")
	 																									.reasonCode("DAMAGED")
	 																									.startDate(LocalDate.now())
	 																									.status(Status.REPLACED)
	 																							.build();
    validator.validateRequest(params);
  }

  @Test(expected = BadRequestException.class)
  public void validate_params_bad_deliverTo_code() {
  	ReplaceBadgeParams params = ReplaceBadgeParams.builder()
					.badgeNumber("72B9HA")
					.deliveryCode("WORK")
					.deliveryOptionCode("FAST")
					.reasonCode("DAMAGED")
					.startDate(LocalDate.now())
					.status(Status.REPLACED)
			.build();
    validator.validateRequest(params);
  }

  @Test(expected = BadRequestException.class)
  public void validate_params_bad_deliverOption_code() {
  	ReplaceBadgeParams params = ReplaceBadgeParams.builder()
					.badgeNumber("72B9HA")
					.deliveryCode("HOME")
					.deliveryOptionCode("COLLECT")
					.reasonCode("DAMAGED")
					.startDate(LocalDate.now())
					.status(Status.REPLACED)
			.build();
    validator.validateRequest(params);
  }

  @Test(expected = BadRequestException.class)
  public void validate_params_bad_reason_code() {
  	ReplaceBadgeParams params = ReplaceBadgeParams.builder()
					.badgeNumber("72B9HA")
					.deliveryCode("HOME")
					.deliveryOptionCode("FAST")
					.reasonCode("UNAVAILABLE")
					.startDate(LocalDate.now())
					.status(Status.REPLACED)
			.build();
    validator.validateRequest(params);
  }
}
