package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.getValidPersonBadgeEntity;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import org.junit.Test;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

public class BadgeCancelRequestValidatorTest {

  private BadgeCancelRequestValidator validator;

  public BadgeCancelRequestValidatorTest() {

    validator =
        new BadgeCancelRequestValidator(
            new ReferenceDataService(BadgeTestFixture.getMockRefDataApiClient()));
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
      assertThat(Objects.requireNonNull(e.getResponse().getBody()).getError().getMessage())
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
      assertThat(Objects.requireNonNull(e.getResponse().getBody()).getError().getMessage())
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
      assertThat(Objects.requireNonNull(e.getResponse().getBody()).getError().getMessage())
          .isEqualTo("Unexpected.cancel.fail");
      // Then 400 thrown
      throw e;
    }
  }
}
