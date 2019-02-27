package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import java.time.LocalDate;
import org.junit.Test;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.ReplaceBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

public class ValidateReplaceBadgeTest {

  private ValidateReplaceBadge validator = new ValidateReplaceBadge(new ReferenceDataService(BadgeTestFixture.getMockRefDataApiClient()));

  @Test
  public void validate_params_ok() {
    ReplaceBadgeParams params =
        ReplaceBadgeParams.builder()
            .badgeNumber("72B9HA")
            .deliveryCode("HOME")
            .deliveryOptionCode("FAST")
            .reasonCode("STOLE")
            .startDate(LocalDate.now())
            .status(Status.REPLACED)
            .build();
    validator.validateRequest(params);
  }

  @Test(expected = BadRequestException.class)
  public void validate_params_bad_deliverTo_code() {
    ReplaceBadgeParams params =
        ReplaceBadgeParams.builder()
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
    ReplaceBadgeParams params =
        ReplaceBadgeParams.builder()
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
    ReplaceBadgeParams params =
        ReplaceBadgeParams.builder()
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
