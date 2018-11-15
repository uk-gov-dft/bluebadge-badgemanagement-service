package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import static uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum.*;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

@Component
@Slf4j
public class ValidateBadgeOrder extends ValidateBase {

  private final ReferenceDataService referenceDataService;

  @Autowired
  ValidateBadgeOrder(ReferenceDataService referenceDataService) {
    this.referenceDataService = referenceDataService;
  }

  public void validate(BadgeEntity entity) {
    log.debug("Validating badge order.");
    List<ErrorErrors> errors = new ArrayList<>();

    // Ref data validation
    validateRefData(PARTY, INVALID_PARTY_CODE, entity.getPartyCode(), errors);
    validateRefData(DELIVER_TO, INVALID_DELIVER_TO_CODE, entity.getDeliverToCode(), errors);
    validateRefData(
        DELIVERY_OPTIONS, INVALID_DELIVER_OPTION_CODE, entity.getDeliverOptionCode(), errors);
    validateRefData(APP_SOURCE, INVALID_CHANNEL_CODE, entity.getAppChannelCode(), errors);
    validateRefData(LA, INVALID_LA_CODE, entity.getLocalAuthorityShortCode(), errors);

    // Business rules
    validateStartDateInFuture(entity, errors);
    validateStartExpiryDateRange(entity, errors);

    // Person specific validation
    if (entity.isPerson()) {
      validateDobInPast(entity, errors);
      validateRefData(ELIGIBILITY, INVALID_ELIGIBILITY_CODE, entity.getEligibilityCode(), errors);
      validateRefData(GENDER, INVALID_GENDER_CODE, entity.getGenderCode(), errors);
    }

    validateNumberOfBadges(entity, errors);

    // Report any failures
    if (!errors.isEmpty()) {
      log.debug("Badge order failed validation.");
      throw new BadRequestException(errors);
    }
    log.debug("Badge order passed validation.");
  }

  private static void validateDobInPast(BadgeEntity entity, List<ErrorErrors> errors) {
    if (null == entity.getDob()) return;

    if (LocalDate.now().isBefore(entity.getDob())) {
      errors.add(ValidationKeyEnum.APP_DATE_IN_PAST.getFieldErrorInstance());
    }
  }

  private static void validateStartDateInFuture(BadgeEntity entity, List<ErrorErrors> errors) {
    // No null check required. Start date mandatory.
    Assert.notNull(entity.getStartDate(), "Start date should not be null.");
    if (LocalDate.now().isAfter(entity.getStartDate())) {
      errors.add(ValidationKeyEnum.START_DATE_IN_PAST.getFieldErrorInstance());
    }
  }

  private static void validateStartExpiryDateRange(BadgeEntity entity, List<ErrorErrors> errors) {
    Assert.notNull(entity.getExpiryDate(), "Expiry date should not be null.");
    if (!(entity.getExpiryDate().minus(Period.ofYears(3)).minus(Period.ofDays(1)))
        .isBefore(entity.getStartDate())) {
      errors.add(ValidationKeyEnum.START_EXPIRY_DATE_RANGE.getFieldErrorInstance());
    }
  }

  private static void validateNumberOfBadges(BadgeEntity entity, List<ErrorErrors> errors) {
    if (entity.isPerson()) {
      if (entity.getNumberOfBadges() != 1) {
        errors.add(ValidationKeyEnum.INVALID_NUMBER_OF_BADGES_PERSON.getFieldErrorInstance());
      }
    } else if (entity.getNumberOfBadges() < 1 || entity.getNumberOfBadges() > 999) {
      errors.add(ValidationKeyEnum.INVALID_NUMBER_OF_BADGES_ORGANISATION.getFieldErrorInstance());
    }
  }

  @Override
  protected ReferenceDataService getReferenceDataService() {
    return referenceDataService;
  }
}
