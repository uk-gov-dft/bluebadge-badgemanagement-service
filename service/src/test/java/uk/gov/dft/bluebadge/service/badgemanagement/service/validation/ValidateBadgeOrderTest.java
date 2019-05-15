package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;
import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.DefaultVals.LOCAL_AUTHORITY_CODE_ENGLAND;
import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.DefaultVals.LOCAL_AUTHORITY_CODE_N_IRELAND;
import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.DefaultVals.LOCAL_AUTHORITY_CODE_SCOTLAND;
import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.DefaultVals.LOCAL_AUTHORITY_CODE_WALES;
import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.getMockRefDataApiClient;
import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.getValidOrgBadgeEntity;
import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.getValidPersonBadgeEntity;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.INVALID_NUMBER_OF_BADGES_ORGANISATION;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.INVALID_NUMBER_OF_BADGES_PERSON;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.START_EXPIRY_DATE_RANGE;

import com.google.common.collect.Sets;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import org.junit.Test;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;
import uk.gov.dft.bluebadge.common.service.enums.EligibilityType;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

public class ValidateBadgeOrderTest {

  private final ValidateBadgeOrder validateBadgeOrder;

  public ValidateBadgeOrderTest() {

    validateBadgeOrder =
        new ValidateBadgeOrder(new ReferenceDataService(getMockRefDataApiClient()));
  }

  @Test
  public void validateEligibilityAndNation() {

    BadgeEntity entity = getValidPersonBadgeEntity();
    entity.setLocalAuthorityShortCode(BadgeTestFixture.DefaultVals.LOCAL_AUTHORITY_CODE_ENGLAND);

    // If eligibility is not CI then validates as ok regardless of nation
    EnumSet<EligibilityType> eligibilities =
        EnumSet.complementOf(EnumSet.of(EligibilityType.COGNITIVE, EligibilityType.TRAF_RISK));
    eligibilities.forEach(
        eligibilityType -> {
          List<ErrorErrors> errors = new ArrayList<>();
          entity.setEligibilityCode(eligibilityType);
          validateBadgeOrder.validateEligibilityAndNation(entity, errors);
          assertThat(errors.size()).isEqualTo(0);
        });

    Set<String> councils =
        Sets.newHashSet(
            LOCAL_AUTHORITY_CODE_WALES,
            LOCAL_AUTHORITY_CODE_ENGLAND,
            LOCAL_AUTHORITY_CODE_SCOTLAND,
            LOCAL_AUTHORITY_CODE_N_IRELAND);

    // If is COGNITIVE, and Welsh LA then ok
    entity.setEligibilityCode(EligibilityType.COGNITIVE);
    councils.forEach(
        council -> {
          entity.setLocalAuthorityShortCode(council);
          List<ErrorErrors> errors = new ArrayList<>();
          validateBadgeOrder.validateEligibilityAndNation(entity, errors);
          if (council.equals(LOCAL_AUTHORITY_CODE_WALES)) {
            assertThat(errors.size()).isEqualTo(0);
          } else {
            assertThat(errors.size()).isEqualTo(1);
            assertThat(errors.get(0).getMessage()).isEqualTo("InvalidNation.badge.eligibilityCode");
          }
        });

    // If is Risk in traffic, and Welsh LA then ok
    entity.setEligibilityCode(EligibilityType.TRAF_RISK);
    councils.forEach(
        council -> {
          entity.setLocalAuthorityShortCode(council);
          List<ErrorErrors> errors = new ArrayList<>();
          validateBadgeOrder.validateEligibilityAndNation(entity, errors);
          if (council.equals(LOCAL_AUTHORITY_CODE_SCOTLAND)) {
            assertThat(errors.size()).isEqualTo(0);
          } else {
            assertThat(errors.size()).isEqualTo(1);
            assertThat(errors.get(0).getMessage()).isEqualTo("InvalidNation.badge.eligibilityCode");
          }
        });
  }

  @Test
  public void validateCreateBadgeRequest_ok() {
    validateBadgeOrder.validate(getValidPersonBadgeEntity());
    // If we get here then was valid, else would have been exception thrown.
  }

  @Test(expected = BadRequestException.class)
  public void validateCreateBadgeRequest_fail() {
    BadgeEntity entity = getValidPersonBadgeEntity();
    entity.setPartyCode("NOT_EXISTS");
    validateBadgeOrder.validate(entity);
    // If we get here then was valid, else would have been exception thrown.
  }

  @Test
  public void validateDobInPast() {

    BadgeEntity entity = getValidPersonBadgeEntity();
    entity.setDob(LocalDate.now().plus(Period.ofDays(1)));
    List<ErrorErrors> errors = new ArrayList<>();
    ValidateBadgeOrder.validateDobInPast(entity, errors);
    assertThat(errors.size()).isEqualTo(1);
    assertThat(errors.get(0).getMessage()).isEqualTo(ValidationKeyEnum.DOB_IN_PAST.getKey());
  }

  @Test
  public void validateApplicationDateInPast() {

    BadgeEntity entity = getValidPersonBadgeEntity();
    entity.setAppDate(LocalDate.now().plus(Period.ofDays(1)));
    List<ErrorErrors> errors = new ArrayList<>();
    ValidateBadgeOrder.validateApplicationDateInPast(entity, errors);
    assertThat(errors.size()).isEqualTo(1);
    assertThat(errors.get(0).getMessage()).isEqualTo(ValidationKeyEnum.APP_DATE_IN_PAST.getKey());
  }

  @Test
  public void validateStartDateInFuture() {

    BadgeEntity entity = getValidPersonBadgeEntity();
    entity.setStartDate(LocalDate.now().minus(Period.ofDays(1)));
    List<ErrorErrors> errors = new ArrayList<>();
    ValidateBadgeOrder.validateStartDateInFuture(entity, errors);
    assertThat(errors.size()).isEqualTo(1);
    assertThat(errors.get(0).getMessage()).isEqualTo(ValidationKeyEnum.START_DATE_IN_PAST.getKey());
  }

  @Test
  public void validateStartExpiryDateRange() {

    BadgeEntity entity = getValidPersonBadgeEntity();
    entity.setStartDate(LocalDate.now().plusDays(10));
    entity.setExpiryDate(LocalDate.now().plusDays(20));
    List<ErrorErrors> errors = new ArrayList<>();
    ValidateBadgeOrder.validateStartExpiryDateRange(entity, errors);
    // Dates ok
    assertThat(errors.size()).isEqualTo(0);

    entity.setExpiryDate((entity.getStartDate().plus(Period.ofYears(3)).plus(Period.ofDays(1))));
    ValidateBadgeOrder.validateStartExpiryDateRange(entity, errors);
    // Dates within 3 years
    assertThat(errors.size()).isEqualTo(1);
    assertThat(errors.get(0).getMessage()).isEqualTo(START_EXPIRY_DATE_RANGE.getKey());
  }

  @Test
  public void validateExpiryDateInFuture() {

    BadgeEntity entity = getValidPersonBadgeEntity();
    entity.setExpiryDate(LocalDate.now().minus(Period.ofDays(1)));
    List<ErrorErrors> errors = new ArrayList<>();
    ValidateBadgeOrder.validateExpiryDateInFuture(entity, errors);
    assertThat(errors.size()).isEqualTo(1);
    assertThat(errors.get(0).getMessage())
        .isEqualTo(ValidationKeyEnum.EXPIRY_DATE_IN_PAST.getKey());
  }

  @Test
  public void validateCreateBadgeRequest_invalid_ref_data() {
    try {
      BadgeEntity entity = getValidPersonBadgeEntity();
      entity.setPartyCode("Bob");
      validateBadgeOrder.validate(entity);
      fail("Ref data validation should throw an exception");
    } catch (BadRequestException e) {
      assertThat(Objects.requireNonNull(e.getResponse().getBody()).getError().getErrors().size())
          .isEqualTo(1);
    }
  }

  @Test
  public void null_stuff() {
    BadgeEntity entity = getValidOrgBadgeEntity();
    // Only other thing that can be null used in validation is dob for an org badge
    entity.setDob(null);
    validateBadgeOrder.validate(entity);
  }

  @Test
  public void validateCreateBadgeRequest_person_nullEligibilityCode() {
    try {
      BadgeEntity entity = getValidPersonBadgeEntity();
      entity.setEligibilityCode(null);
      validateBadgeOrder.validate(entity);
      fail("Badge valid number of budges validation should throw an exception");
    } catch (BadRequestException e) {
      Error error = Objects.requireNonNull(e.getResponse().getBody()).getError();
      assertThat(error.getErrors().size()).isEqualTo(1);
      ErrorErrors errorErrors = error.getErrors().get(0);
      assertThat(errorErrors.getField()).isEqualTo("eligibilityCode");
      assertThat(errorErrors.getMessage()).isEqualTo("NotNull.badge.eligibilityCode");
    }
  }

  @Test
  public void validateNumberOfBadges() {

    BadgeEntity entity = getValidPersonBadgeEntity();
    entity.setNumberOfBadges(10);
    List<ErrorErrors> errors = new ArrayList<>();
    ValidateBadgeOrder.validateNumberOfBadges(entity, errors);
    // Person badge must be 1
    assertThat(errors.size()).isEqualTo(1);
    assertThat(errors.get(0).getMessage()).isEqualTo(INVALID_NUMBER_OF_BADGES_PERSON.getKey());

    errors.remove(0);
    entity.setPartyCode("ORG");
    entity.setNumberOfBadges(0);
    ValidateBadgeOrder.validateNumberOfBadges(entity, errors);
    // Org number of badges between 1 and 999
    assertThat(errors.size()).isEqualTo(1);
    assertThat(errors.get(0).getMessage())
        .isEqualTo(INVALID_NUMBER_OF_BADGES_ORGANISATION.getKey());

    errors.remove(0);
    entity.setNumberOfBadges(1000);
    ValidateBadgeOrder.validateNumberOfBadges(entity, errors);
    // Org number of badges between 1 and 999
    assertThat(errors.size()).isEqualTo(1);
    assertThat(errors.get(0).getMessage())
        .isEqualTo(INVALID_NUMBER_OF_BADGES_ORGANISATION.getKey());
  }

  @Test
  public void validateDeliveryRules() {

    BadgeEntity entity = getValidPersonBadgeEntity();
    entity.setDeliverToCode("COUNCIL");
    entity.setDeliverOptionCode("FAST");
    List<ErrorErrors> errors = new ArrayList<>();
    ValidateBadgeOrder.validateDeliveryRules(entity, errors);
    assertThat(errors.size()).isEqualTo(1);
    assertThat(errors.get(0).getMessage()).isEqualTo("Invalid.badge.deliverOptionCode");
  }

  @Test
  public void validateOrganisationBadgeWithMissingContactName() {
    BadgeEntity entity = getValidOrgBadgeEntity();
    entity.setContactName(null);
    try {
      validateBadgeOrder.validate(entity);
      fail("No Exception thrown");
    } catch (BadRequestException e) {
      List<ErrorErrors> errors =
          Objects.requireNonNull(e.getResponse().getBody()).getError().getErrors();
      assertThat(errors.size()).isEqualTo(1);
      assertThat(errors.get(0).getMessage()).isEqualTo("NotNull.badge.party.contact.fullName");
    }
  }

  @Test
  public void validateInvalidPartyTypeWithMissingContactName() {
    BadgeEntity entity = getValidOrgBadgeEntity();
    entity.setPartyCode("WRONG");
    entity.setContactName(null);
    try {
      validateBadgeOrder.validate(entity);
      fail("No Exception thrown");
    } catch (BadRequestException e) {
      List<ErrorErrors> errors =
          Objects.requireNonNull(e.getResponse().getBody()).getError().getErrors();
      assertThat(errors.size()).isEqualTo(1);
      assertThat(errors.get(0).getMessage()).isEqualTo("Invalid.badge.partyCode");
    }
  }

  @Test
  public void whenOrderIsForOrganisation_thenRejectAnyValueOfNotForReassessment() {
    BadgeEntity entity = getValidOrgBadgeEntity();
    entity.setNotForReassessment(new Random().nextBoolean());
    try {
      validateBadgeOrder.validate(entity);
      fail("No Exception thrown");
    } catch (BadRequestException e) {
      List<ErrorErrors> errors = Objects.requireNonNull(
          e.getResponse().getBody().getError().getErrors()
      );
      assertThat(errors.size()).isEqualTo(1);
      assertThat(errors.get(0).getMessage()).isEqualTo("Invalid.badge.notForReassessment");
    }
  }

  @Test
  public void whenPersonIsAutomaticEligible_thenRejectAnyValueOfNotForReassessment() {
    BadgeEntity entity = getValidPersonBadgeEntity();
    entity.setEligibilityCode(getRandomlyAutomaticEligibilityCode());
    entity.setNotForReassessment(new Random().nextBoolean());
    try {
      validateBadgeOrder.validate(entity);
      fail("No Exception thrown");
    } catch (BadRequestException e) {
      List<ErrorErrors> errors = Objects.requireNonNull(
          e.getResponse().getBody().getError().getErrors()
      );
      assertThat(errors.size()).isEqualTo(1);
      assertThat(errors.get(0).getMessage()).isEqualTo("Invalid.badge.notForReassessment");
    }
  }

  private EligibilityType getRandomlyAutomaticEligibilityCode() {
    final EligibilityType[] CODES = {
        EligibilityType.PIP,
        EligibilityType.DLA,
        EligibilityType.BLIND,
        EligibilityType.AFRFCS,
        EligibilityType.WPMS
    };

    return CODES[new Random().nextInt(CODES.length)];
  }
}
