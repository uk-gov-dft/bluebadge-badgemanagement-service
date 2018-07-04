package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static uk.gov.dft.bluebadge.service.badgemanagement.service.RefDataGroupEnum.*;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.RefDataGroupEnum.ELIGIBILITY;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.ValidationKeyEnum.*;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.ValidationKeyEnum.INVALID_ELIGIBILITY_CODE;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.ErrorErrors;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.exception.BadRequestException;

public class ValidateBadgeOrder {

  private ValidateBadgeOrder() {}

  public static void validateCreateBadgeRequest(BadgeEntity entity) {
    List<ErrorErrors> errors = new ArrayList<>();
    boolean isPerson = entity.getPartyCode().equals("PERSON");

    // Ref data validation
    validateRefData(PARTY, INVALID_PARTY_CODE, entity.getPartyCode(), errors);
    validateRefData(DELIVER_TO, INVALID_DELIVER_TO_CODE, entity.getDeliverToCode(), errors);
    validateRefData(
        DELIVERY_OPTIONS, INVALID_DELIVER_OPTION_CODE, entity.getDeliverOptionCode(), errors);
    validateRefData(APP_SOURCE, INVALID_CHANNEL_CODE, entity.getAppChannelCode(), errors);

    // Business rules
    validateStartDateInFuture(entity, errors);
    validateStartExpiryDateRange(entity, errors);

    // Person specific validation
    if (isPerson) {
      validateDobInPast(entity, errors);
      validateRefData(ELIGIBILITY, INVALID_ELIGIBILITY_CODE, entity.getEligibilityCode(), errors);
    }
    if (errors.size() > 0) {
      throw new BadRequestException(errors);
    }
  }

  private static void validateDobInPast(BadgeEntity entity, List<ErrorErrors> errors) {
    if (null == entity.getDob()) return;

    if (LocalDate.now().isBefore(entity.getDob())) {
      errors.add(ValidationKeyEnum.APP_DATE_IN_PAST.getErrorInstance());
    }
  }

  private static void validateStartDateInFuture(BadgeEntity entity, List<ErrorErrors> errors) {
    // No null check required. Start date mandatory.
    Assert.notNull(entity.getStartDate(), "Start date should not be null.");
    if (LocalDate.now().isAfter(entity.getStartDate())) {
      errors.add(ValidationKeyEnum.START_DATE_IN_PAST.getErrorInstance());
    }
  }

  private static void validateStartExpiryDateRange(BadgeEntity entity, List<ErrorErrors> errors) {
    Assert.notNull(entity.getExpiryDate(), "Expiry date should not be null.");
    if (entity.getExpiryDate().minus(Period.ofYears(3)).isBefore(entity.getStartDate())) {
      errors.add(ValidationKeyEnum.START_EXPIRY_DATE_RANGE.getErrorInstance());
    }
  }

  private static void validateRefData(
      RefDataGroupEnum group,
      ValidationKeyEnum validationKeyEnum,
      String value,
      List<ErrorErrors> errors) {
    if (null == value) return;

    if (!RefData.groupContainsKey(group, value)) {
      errors.add(validationKeyEnum.getErrorInstance());
    }
  }
}
