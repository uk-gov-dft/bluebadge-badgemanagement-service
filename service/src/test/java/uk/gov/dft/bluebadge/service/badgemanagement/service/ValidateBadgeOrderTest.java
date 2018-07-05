package uk.gov.dft.bluebadge.service.badgemanagement.service;

import java.time.LocalDate;
import java.time.Period;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestBase;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.exception.BadRequestException;

public class ValidateBadgeOrderTest extends BadgeTestBase {

  @Test
  public void validateCreateBadgeRequest_ok() {
    try {
      ValidateBadgeOrder.validateCreateBadgeRequest(getValidBadge());
      // If we get here then was valid, else would have been exception thrown.
    } catch (BadRequestException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }

  @Test
  public void validateCreateBadgeRequest_dob_not_in_past() {
    try {
      BadgeEntity entity = getValidBadge();
      entity.setDob(LocalDate.now().plus(Period.ofDays(1)));
      ValidateBadgeOrder.validateCreateBadgeRequest(entity);
      Assert.fail("DOB validation should throw an exception");
    } catch (BadRequestException e) {
      Assert.assertEquals(1, e.getResponse().getBody().getError().getErrors().size());
    }
  }

  @Test
  public void validateCreateBadgeRequest_start_date_in_future() {
    try {
      BadgeEntity entity = getValidBadge();
      entity.setStartDate(LocalDate.now().minus(Period.ofDays(1)));
      ValidateBadgeOrder.validateCreateBadgeRequest(entity);
      Assert.fail("Start date validation should throw an exception");
    } catch (BadRequestException e) {
      Assert.assertEquals(1, e.getResponse().getBody().getError().getErrors().size());
    }
  }

  @Test
  public void validateCreateBadgeRequest_start_expiry_range() {
    try {
      BadgeEntity entity = getValidBadge();
      entity.setExpiryDate((entity.getStartDate().plus(Period.ofYears(3)).plus(Period.ofDays(1))));
      ValidateBadgeOrder.validateCreateBadgeRequest(entity);
      Assert.fail("Badge valid range validation should throw an exception");
    } catch (BadRequestException e) {
      Assert.assertEquals(1, e.getResponse().getBody().getError().getErrors().size());
    }
  }

  @Test
  public void validateCreateBadgeRequest_invalid_ref_data() {
    try {
      BadgeEntity entity = getValidBadge();
      entity.setPartyCode("Bob");
      ValidateBadgeOrder.validateCreateBadgeRequest(entity);
      Assert.fail("Ref data validation should throw an exception");
    } catch (BadRequestException e) {
      Assert.assertEquals(1, e.getResponse().getBody().getError().getErrors().size());
    }
  }

  @Test
  public void null_stuff() {
    BadgeEntity entity = getValidBadge();
    // Try a null ref data look up
    entity.setEligibilityCode(null);
    ValidateBadgeOrder.validateCreateBadgeRequest(entity);
    // Only other thing that can be null used in validation is dob.
    entity.setDob(null);
    ValidateBadgeOrder.validateCreateBadgeRequest(entity);
  }
}
