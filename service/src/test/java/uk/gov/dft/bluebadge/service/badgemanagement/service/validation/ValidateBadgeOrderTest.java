package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Period;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestBase;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;

public class ValidateBadgeOrderTest extends BadgeTestBase {

  private ValidateBadgeOrder validateBadgeOrder;

  public ValidateBadgeOrderTest() {
    super();
    validateBadgeOrder = new ValidateBadgeOrder(referenceDataService);
  }

  @Test
  public void validateCreateBadgeRequest_ok() {
    try {
      validateBadgeOrder.validate(getValidPersonBadgeEntity());
      // If we get here then was valid, else would have been exception thrown.
    } catch (BadRequestException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }

  @Test
  public void validateCreateBadgeRequest_dob_not_in_past() {
    try {
      BadgeEntity entity = getValidPersonBadgeEntity();
      entity.setDob(LocalDate.now().plus(Period.ofDays(1)));
      validateBadgeOrder.validate(entity);
      Assert.fail("DOB validation should throw an exception");
    } catch (BadRequestException e) {
      Assert.assertEquals(1, e.getResponse().getBody().getError().getErrors().size());
    }
  }

  @Test
  public void validateCreateBadgeRequest_start_date_in_future() {
    try {
      BadgeEntity entity = getValidPersonBadgeEntity();
      entity.setStartDate(LocalDate.now().minus(Period.ofDays(1)));
      validateBadgeOrder.validate(entity);
      Assert.fail("Start date validation should throw an exception");
    } catch (BadRequestException e) {
      Assert.assertEquals(1, e.getResponse().getBody().getError().getErrors().size());
    }
  }

  @Test
  public void validateCreateBadgeRequest_start_expiry_range() {
    try {
      BadgeEntity entity = getValidPersonBadgeEntity();
      entity.setExpiryDate((entity.getStartDate().plus(Period.ofYears(3)).plus(Period.ofDays(1))));
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
    // Try a null ref data look up
    entity.setEligibilityCode(null);
    validateBadgeOrder.validate(entity);
    // Only other thing that can be null used in validation is dob.
    entity.setDob(null);
    validateBadgeOrder.validate(entity);
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
