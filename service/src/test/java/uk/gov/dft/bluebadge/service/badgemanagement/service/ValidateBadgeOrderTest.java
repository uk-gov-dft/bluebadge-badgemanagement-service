package uk.gov.dft.bluebadge.service.badgemanagement.service;

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
}
