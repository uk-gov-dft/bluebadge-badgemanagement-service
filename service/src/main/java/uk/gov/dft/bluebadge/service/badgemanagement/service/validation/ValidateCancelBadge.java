package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import static uk.gov.dft.bluebadge.common.service.exception.NotFoundException.Operation.UPDATE;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status.ISSUED;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status.ORDERED;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.CANCEL_EXPIRY_DATE_IN_PAST;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.CANCEL_FAILED_UNEXPECTED;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.CANCEL_STATUS_INVALID;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

@Slf4j
@Component
public class ValidateCancelBadge extends ValidateBase {

  private final ReferenceDataService referenceDataService;

  @Autowired
  public ValidateCancelBadge(ReferenceDataService referenceDataService) {
    this.referenceDataService = referenceDataService;
  }

  @Override
  protected ReferenceDataService getReferenceDataService() {
    return referenceDataService;
  }

  /**
   * Validate a cancel request. Throws exception resulting in 400 on failure.
   *
   * @param request Cancel request.
   */
  public void validateRequest(CancelBadgeParams request) {
    log.debug("Validating cancel request for {}", request.getBadgeNo());
    List<ErrorErrors> errors = new ArrayList<>();
    validateRefData(
        RefDataGroupEnum.CANCEL,
        ValidationKeyEnum.INVALID_CANCEL_CODE,
        request.getCancelReasonCode(),
        errors);

    if (!errors.isEmpty()) {
      throw new BadRequestException(errors);
    }
  }

  /**
   * Determine the reason why cancel failed.
   *
   * @param badgeEntity Entity that could not be cancelled.
   */
  public void validateAfterFailedCancel(BadgeEntity badgeEntity) {
    // Retrieve badge (if possible) and return error from above.
    // 1. PrintBatchBadgeRequest does not exist
    if (null == badgeEntity) {
      throw new NotFoundException("badge", UPDATE);
    }
    log.debug("Validating why cancel failed for {}", badgeEntity.getBadgeNo());

    // 2. PrintBatchBadgeRequest already expired.
    validateExpiryDateInFuture(badgeEntity);

    // 3. PrintBatchBadgeRequest status invalid.
    validateStatusValidForCancel(badgeEntity);

    // If got here then it is a bit of a mystery why cancel did not work
    log.error("Cancel badge failed for unexpected reason. PrintBatchBadgeRequest: {}", badgeEntity.getBadgeNo());
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
