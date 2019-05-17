package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.DefaultVals.PRIMARY_PHONE_NUMBER_TRIMMED;
import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.DefaultVals.SECONDARY_PHONE_NUMBER_TRIMMED;
import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.getValidBadgeOrderOrgRequest;
import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.getValidBadgeOrderPersonRequest;

import java.util.Random;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.dft.bluebadge.common.service.enums.EligibilityType;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;

public class BadgeOrderRequestConverterTest {

  @Test
  public void convertToEntity_Person() {
    BadgeOrderRequest request = getValidBadgeOrderPersonRequest();
    // To check spaces stripped and upper case
    request.getParty().getContact().setPostCode("wv16 4aw");

    BadgeOrderRequestConverter converter = new BadgeOrderRequestConverter();
    BadgeEntity entity = converter.convertToEntity(request);

    Assert.assertEquals("WV164AW", entity.getContactPostcode());
    Assert.assertEquals(
        request.getParty().getPerson().getBadgeHolderName(), entity.getHolderName());
    Assert.assertNotNull(entity.getOrderDate());
    Assert.assertEquals(PRIMARY_PHONE_NUMBER_TRIMMED, entity.getPrimaryPhoneNo());
    Assert.assertEquals(SECONDARY_PHONE_NUMBER_TRIMMED, entity.getSecondaryPhoneNo());
  }

  @Test
  public void convertToEntity_mixedCaseSpacedOutNino() {
    BadgeOrderRequest request = getValidBadgeOrderPersonRequest();
    // To check spaces stripped and upper case
    request.getParty().getPerson().setNino("Ns 12 34 56 a   ");

    BadgeOrderRequestConverter converter = new BadgeOrderRequestConverter();
    BadgeEntity entity = converter.convertToEntity(request);

    Assert.assertEquals("NS123456A", entity.getNino());
  }

  @Test
  public void convertToEntity_Org() {
    BadgeOrderRequest request = getValidBadgeOrderOrgRequest();
    // To check spaces stripped and upper case
    request.getParty().getContact().setPostCode("wv16 4aw");

    BadgeOrderRequestConverter converter = new BadgeOrderRequestConverter();
    BadgeEntity entity = converter.convertToEntity(request);

    Assert.assertEquals("WV164AW", entity.getContactPostcode());
    Assert.assertEquals(
        request.getParty().getOrganisation().getBadgeHolderName(), entity.getHolderName());
    Assert.assertEquals(PRIMARY_PHONE_NUMBER_TRIMMED, entity.getPrimaryPhoneNo());
    Assert.assertEquals(null, entity.getSecondaryPhoneNo());
  }

  @Test(expected = BadRequestException.class)
  public void convertToEntity_no_person() {
    BadgeOrderRequest request = getValidBadgeOrderOrgRequest();
    request.getParty().setTypeCode("PERSON");
    request.getParty().setPerson(null);
    BadgeOrderRequestConverter converter = new BadgeOrderRequestConverter();
    converter.convertToEntity(request);
  }

  @Test(expected = BadRequestException.class)
  public void convertToEntity_no_org() {
    BadgeOrderRequest request = getValidBadgeOrderOrgRequest();
    request.getParty().setTypeCode("ORG");
    request.getParty().setOrganisation(null);
    BadgeOrderRequestConverter converter = new BadgeOrderRequestConverter();
    converter.convertToEntity(request);
  }

  @Test
  public void whenPersonBadgeOrderRequestHasNotForReassessmentAsTrue_thenEntityMustAlsoHaveIt() {
    BadgeOrderRequest request = getValidBadgeOrderPersonRequest();
    request.setNotForReassessment(true);
    BadgeEntity entity = new BadgeOrderRequestConverter().convertToEntity(request);
    Assert.assertTrue(entity.getNotForReassessment());
  }

  @Test
  public void whenPersonBadgeOrderRequestHasNotForReassessmentAsFalse_thenEntityMustAlsoHaveIt() {
    BadgeOrderRequest request = getValidBadgeOrderPersonRequest();
    request.setNotForReassessment(false);
    BadgeEntity entity = new BadgeOrderRequestConverter().convertToEntity(request);
    Assert.assertFalse(entity.getNotForReassessment());
  }

  @Test
  public void whenOrgBadgeOrderRequestHasNotForReassessmentAsTrue_thenEntityMustAlsoHaveIt() {
    BadgeOrderRequest request = getValidBadgeOrderOrgRequest();
    request.setNotForReassessment(true);
    BadgeEntity entity = new BadgeOrderRequestConverter().convertToEntity(request);
    Assert.assertTrue(entity.getNotForReassessment());
  }

  @Test
  public void whenOrgBadgeOrderRequestHasNotForReassessmentAsFalse_thenEntityMustAlsoHaveIt() {
    BadgeOrderRequest request = getValidBadgeOrderOrgRequest();
    request.setNotForReassessment(false);
    BadgeEntity entity = new BadgeOrderRequestConverter().convertToEntity(request);
    Assert.assertFalse(entity.getNotForReassessment());
  }

  @Test
  public void whenOrgBadgeOrderRequestDoesNotHaveNotForReassessment_thenEntityMustNotHaveIt() {
    BadgeOrderRequest request = getValidBadgeOrderOrgRequest();
    request.setNotForReassessment(null);
    BadgeEntity entity = new BadgeOrderRequestConverter().convertToEntity(request);
    Assert.assertNull(entity.getNotForReassessment());
  }

  @Test
  public void whenAutomaticEligiblePersonRequestDoesNotHaveNotForReassessment_thenEntityMustNotHaveIt() {
    BadgeOrderRequest request = getValidBadgeOrderPersonRequest();
    request.setNotForReassessment(null);
    BadgeEntity entity = new BadgeOrderRequestConverter().convertToEntity(request);
    Assert.assertNull(entity.getNotForReassessment());
  }

  @Test
  public void whenNonAutomaticEligiblePersonRequestDoesNotHaveNotForReassessment_thenEntityMustNotHaveIt() {
    BadgeOrderRequest request = getValidBadgeOrderPersonRequest();
    request.setEligibilityCode(EligibilityType.CHILDVEHIC);
    request.setNotForReassessment(null);
    BadgeEntity entity = new BadgeOrderRequestConverter().convertToEntity(request);
    Assert.assertFalse(entity.getNotForReassessment());
  }
}
