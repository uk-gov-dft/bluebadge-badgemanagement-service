package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import org.junit.Test;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.getMockRefDataApiClient;
import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.getValidPersonBadgeEntity;

public class ValidateBadgeOrderTest {

  private ValidateBadgeOrder validateBadgeOrder;

  public ValidateBadgeOrderTest() {

    validateBadgeOrder =
        new ValidateBadgeOrder(new ReferenceDataService(getMockRefDataApiClient()));
  }

  @Test
  public void validateCognitiveImpairment() {

    ReferenceDataService refDataServiceMock = mock(ReferenceDataService.class);
    ValidateBadgeOrder validator = new ValidateBadgeOrder(refDataServiceMock);
    List<ErrorErrors> errors = new ArrayList<>();

    // If eligibility is not CI then validates as ok
    validator.validateCognitiveImpairment(getValidPersonBadgeEntity(), errors);
  }

  @Test
  public void validateCreateBadgeRequest_ok() {
    validateBadgeOrder.validate(getValidPersonBadgeEntity());
    // If we get here then was valid, else would have been exception thrown.
  }

  @Test(expected = BadRequestException.class)
  public void validateCreateBadgeRequest_fail() {
    BadgeEntity entity = getValidPersonBadgeEntity();
    entity.setEligibilityCode("NOT_EXISTS");
    validateBadgeOrder.validate(getValidPersonBadgeEntity());
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
  public void validateCreateBadgeRequest_start_expiry_range() {

      // Dates ok
      BadgeEntity entity = getValidPersonBadgeEntity();
      entity.setStartDate(LocalDate.now().plusDays(10));
      entity.setExpiryDate(LocalDate.now().plusDays(20));
      List<ErrorErrors> errors = new ArrayList<>();
      ValidateBadgeOrder.validateStartExpiryDateRange(entity, errors);
    assertThat(errors.size()).isEqualTo(0);

    try {
      // Dates more than 3 years apart
      BadgeEntity entity = getValidPersonBadgeEntity();
      entity.setExpiryDate((entity.getStartDate().plus(Period.ofYears(3)).plus(Period.ofDays(1))));
      validateBadgeOrder.validate(entity);
      Assert.fail("Badge valid range validation should throw an exception");
    } catch (BadRequestException e) {
      Assert.assertEquals(1, e.getResponse().getBody().getError().getErrors().size());
    }

    try {
      // Expiry in past
      BadgeEntity entity = getValidPersonBadgeEntity();
      entity.setStartDate(LocalDate.now().plusDays(10));
      entity.setExpiryDate(LocalDate.now().minusDays(10));
      validateBadgeOrder.validate(entity);
      Assert.fail("Badge valid range validation should throw an exception");
    } catch (BadRequestException e) {
      Assert.assertEquals(1, e.getResponse().getBody().getError().getErrors().size());
    }
  }

  @Test
  public void validateCreateBadgeRequest_invalid_ref_data() {
    try {
      BadgeEntity entity = getValidPersonBadgeEntity();
      entity.setPartyCode("Bob");
      validateBadgeOrder.validate(entity);
      Assert.fail("Ref data validation should throw an exception");
    } catch (BadRequestException e) {
      Assert.assertEquals(1, e.getResponse().getBody().getError().getErrors().size());
    }
  }

  @Test
  public void null_stuff() {
    BadgeEntity entity = getValidPersonBadgeEntity();
    // Only other thing that can be null used in validation is dob.
    entity.setDob(null);
    validateBadgeOrder.validate(entity);
  }

  @Test
  public void validateCreateBadgeRequest_person_nullEligibilityCode() {
    try {
      BadgeEntity entity = getValidPersonBadgeEntity();
      entity.setEligibilityCode(null);
      validateBadgeOrder.validate(entity);
      Assert.fail("Badge valid number of budges validation should throw an exception");
    } catch (BadRequestException e) {
      Error error = e.getResponse().getBody().getError();
      Assert.assertEquals(1, error.getErrors().size());
      ErrorErrors errorErrors = error.getErrors().get(0);
      Assert.assertEquals("eligibilityCode", errorErrors.getField());
      Assert.assertEquals(
          "Eligibility code is mandatory for Person badges.", errorErrors.getReason());
      Assert.assertEquals("NotNull.badge.eligibilityCode", errorErrors.getMessage());
    }
  }

  @Test
  public void validateCreateBadgeRequest_person_incorrectNumberOfBadges() {
    try {
      BadgeEntity entity = getValidPersonBadgeEntity();
      entity.setNumberOfBadges(10);
      validateBadgeOrder.validate(entity);
      Assert.fail("Badge valid number of budges validation should throw an exception");
    } catch (BadRequestException e) {
      Assert.assertEquals(1, e.getResponse().getBody().getError().getErrors().size());
    }
  }

  @Test
  public void validateCreateBadgeRequest_fastDelivery_toCouncil() {
    try {
      BadgeEntity entity = getValidPersonBadgeEntity();
      entity.setDeliverToCode("COUNCIL");
      entity.setDeliverOptionCode("FAST");
      validateBadgeOrder.validate(entity);
      Assert.fail("Badge deivery rules validation should throw an exception");
    } catch (BadRequestException e) {
      assertEquals(1, e.getResponse().getBody().getError().getErrors().size());
      String message = e.getResponse().getBody().getError().getErrors().get(0).getMessage();
      String reason = e.getResponse().getBody().getError().getErrors().get(0).getReason();

      assertEquals(
          "Only 'standard' delivery option is available when delivering to council.", reason);
      assertEquals("Invalid.badge.deliverOptionCode", message);
    }
  }
}
