package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import static uk.gov.dft.bluebadge.common.service.exception.NotFoundException.Operation.UPDATE;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status.ISSUED;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status.ORDERED;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.CANCEL_EXPIRY_DATE_IN_PAST;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.CANCEL_FAILED_UNEXPECTED;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.CANCEL_STATUS_INVALID;

import java.time.LocalDate;
import java.util.EnumSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

@Slf4j
@Component
public class BadgeCancelRequestValidator extends ValidateBase {

  private final ReferenceDataService referenceDataService;

  @Autowired
  BadgeCancelRequestValidator(ReferenceDataService referenceDataService) {
    this.referenceDataService = referenceDataService;
  }

  @Override
  protected ReferenceDataService getReferenceDataService() {
    return referenceDataService;
  }

  /**
   * Determine the reason why cancel failed.
   *
   * @param badgeEntity Entity that could not be cancelled.
   */
  public void validateAfterFailedCancel(BadgeEntity badgeEntity) {
    // Retrieve badge (if possible) and return error from above.
    // 1. Badge does not exist
    if (null == badgeEntity) {
      throw new NotFoundException("badge", UPDATE);
    }
    log.debug("Validating why cancel failed for {}", badgeEntity.getBadgeNo());

    // 2. Badge already expired.
    validateExpiryDateInFuture(badgeEntity);

    // 3. Badge status invalid.
    validateStatusValidForCancel(badgeEntity);

    // If got here then it is a bit of a mystery why cancel did not work
    throw new BadRequestException(CANCEL_FAILED_UNEXPECTED.getSystemErrorInstance());
  }

  private void validateStatusValidForCancel(BadgeEntity badgeEntity) {
    if (!EnumSet.of(ISSUED, ORDERED).contains(badgeEntity.getBadgeStatus())) {
      throw new BadRequestException(CANCEL_STATUS_INVALID.getSystemErrorInstance());
    }
  }

  private static void validateExpiryDateInFuture(BadgeEntity entity) {
    // No null check required. Expiry date mandatory.
    Assert.notNull(entity.getExpiryDate(), "Expiry date should not be null.");
    if (LocalDate.now().isAfter(entity.getExpiryDate())) {
      throw new BadRequestException(CANCEL_EXPIRY_DATE_IN_PAST.getSystemErrorInstance());
    }
  }
}
