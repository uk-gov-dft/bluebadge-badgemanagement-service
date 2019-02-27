package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.getValidPersonBadgeEntity;

import java.time.LocalDate;
import java.time.Period;
import org.junit.Test;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

public class ValidateCancelBadgeTest{

  ValidateCancelBadge validator;

  public ValidateCancelBadgeTest() {

    validator = new ValidateCancelBadge(new ReferenceDataService(BadgeTestFixture.getMockRefDataApiClient()));
  }

  @Test
  public void validate_params_ok() {
    // Given valid cancel params
    CancelBadgeParams params =
        CancelBadgeParams.builder()
            .badgeNo("ABCABC")
            .cancelReasonCode(BadgeTestFixture.DefaultVals.CANCEL_CODE_VALID)
            .build();
    // When validated
    validator.validateRequest(params);
    // Then no exception thrown
  }

  @Test(expected = BadRequestException.class)
  public void validate_params_bad() {
    // Given valid cancel params
    CancelBadgeParams params =
        CancelBadgeParams.builder().badgeNo("ABCABC").cancelReasonCode("INVALIDCODEXXX").build();
    // When validated
    validator.validateRequest(params);
    // Then exception thrown
  }

  @Test(expected = NotFoundException.class)
  public void validateWhyCancelFailed_badge_not_exists() {
    // Given badge to cancel did not exist
    // When validated
    validator.validateAfterFailedCancel(null);
    // Then 404 thrown
  }

  @Test(expected = BadRequestException.class)
  public void validateWhyCancelFailed_badge_not_issued() {
    // Given badge status not ISSUED
    BadgeEntity badge = getValidPersonBadgeEntity();
    badge.setBadgeStatus(BadgeEntity.Status.REPLACED);
    badge.setExpiryDate(LocalDate.now().plus(Period.ofMonths(1)));

    // When validated
    try {
      validator.validateAfterFailedCancel(badge);
    } catch (BadRequestException e) {
      assertThat(e.getResponse().getBody().getError().getMessage())
          .isEqualTo("Invalid.badge.cancel.status");
      // Then 400 thrown
      throw e;
    }
  }

  @Test(expected = BadRequestException.class)
  public void validateWhyCancelFailed_badge_already_expired() {
    // Given badge status not ISSUED
    BadgeEntity badge = getValidPersonBadgeEntity();
    badge.setExpiryDate(LocalDate.now().minus(Period.ofMonths(1)));

    // When validated
    try {
      validator.validateAfterFailedCancel(badge);
    } catch (BadRequestException e) {
      assertThat(e.getResponse().getBody().getError().getMessage())
          .isEqualTo("Invalid.badge.cancel.expiryDate");
      // Then 400 thrown
      throw e;
    }
  }

  @Test(expected = BadRequestException.class)
  public void validateWhyCancelFailed_unexpected() {
    // Given badge status not ISSUED
    BadgeEntity badge = getValidPersonBadgeEntity();
    badge.setExpiryDate(LocalDate.now().plus(Period.ofMonths(1)));

    // When validated
    try {
      validator.validateAfterFailedCancel(badge);
    } catch (BadRequestException e) {
      assertThat(e.getResponse().getBody().getError().getMessage())
          .isEqualTo("Unexpected.cancel.fail");
      // Then 400 thrown
      throw e;
    }
  }
}
