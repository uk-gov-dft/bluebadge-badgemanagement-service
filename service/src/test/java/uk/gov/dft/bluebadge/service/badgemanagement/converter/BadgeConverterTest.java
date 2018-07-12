package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Badge;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestBase;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;

public class BadgeConverterTest extends BadgeTestBase {

  @Test
  public void convertToModel_person() {
    // Given a valid person badge
    BadgeEntity entity = getValidPersonBadgeEntity();
    // When converted to model
    Badge model = new BadgeConverter().convertToModel(entity);
    // Then person data is present but not organisation
    Assert.assertEquals(model.getParty().getPerson().getBadgeHolderName(), entity.getHolderName());
    Assert.assertNull(model.getParty().getOrganisation());
    // And contact details present.
    Assert.assertEquals(
        model.getParty().getContact().getPrimaryPhoneNumber(), entity.getPrimaryPhoneNo());
  }

  @Test
  public void convertToModel_org() {
    // Given a valid person badge
    BadgeEntity entity = getValidOrgBadgeEntity();
    // When converted to model
    Badge model = new BadgeConverter().convertToModel(entity);
    // Then person data is present but not organisation
    Assert.assertEquals(
        model.getParty().getOrganisation().getBadgeHolderName(), entity.getHolderName());
    Assert.assertNull(model.getParty().getPerson());
    // And contact details present.
    Assert.assertEquals(
        model.getParty().getContact().getPrimaryPhoneNumber(), entity.getPrimaryPhoneNo());
  }
}
