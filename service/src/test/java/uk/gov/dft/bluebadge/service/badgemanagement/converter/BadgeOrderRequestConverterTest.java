package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestBase;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;

public class BadgeOrderRequestConverterTest extends BadgeTestBase {

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
}
