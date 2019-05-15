package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.getValidOrgBadgeEntity;
import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.getValidPersonBadgeEntity;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Badge;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;

public class BadgeConverterTest {

  @Test
  public void convertToModel_person() {
    // Given a valid person badge
    BadgeEntity entity = getValidPersonBadgeEntity();
    // When converted to model
    Badge model = new BadgeConverter().convertToModel(entity);
    // Then person data is present but not organisation
    Assert.assertEquals(entity.getHolderName(), model.getParty().getPerson().getBadgeHolderName());
    Assert.assertNull(model.getParty().getOrganisation());
    // And contact details present.
    Assert.assertEquals(
        model.getParty().getContact().getPrimaryPhoneNumber(), entity.getPrimaryPhoneNo());
    Assert.assertEquals(BadgeTestFixture.DefaultVals.REPLACE_REASON, model.getReplaceReasonCode());
  }

  @Test
  public void convertToModel_org() {
    // Given a valid person badge
    BadgeEntity entity = getValidOrgBadgeEntity();
    // When converted to model
    Badge model = new BadgeConverter().convertToModel(entity);
    // Then person data is present but not organisation
    Assert.assertEquals(
        entity.getHolderName(), model.getParty().getOrganisation().getBadgeHolderName());
    Assert.assertNull(model.getParty().getPerson());
    // And contact details present.
    Assert.assertEquals(
        entity.getPrimaryPhoneNo(), model.getParty().getContact().getPrimaryPhoneNumber());
  }

  @Test
  public void whenNotForReassessmentExistsinEntity_thenModelAlsoHasNotForReassessment() {
    BadgeEntity entity = getValidPersonBadgeEntity();
    Badge model = new BadgeConverter().convertToModel(entity);

    Assert.assertEquals(entity.isNotForReassessment(), model.isNotForReassessment());
  }
}
