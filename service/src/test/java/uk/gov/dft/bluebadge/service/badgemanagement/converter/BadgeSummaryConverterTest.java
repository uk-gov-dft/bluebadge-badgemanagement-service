package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeSummary;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestBase;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;

public class BadgeSummaryConverterTest extends BadgeTestBase {

  @Test
  public void convertToModel_person() {
    BadgeEntity entity = getValidPersonBadgeEntity();
    BadgeSummaryConverter converter = new BadgeSummaryConverter();
    BadgeSummary model = converter.convertToModel(entity);

    // Check a few values.
    Assert.assertEquals(entity.getBadgeNo(), model.getBadgeNumber());
    Assert.assertEquals(entity.getBadgeStatus().name(), model.getStatusCode());
    Assert.assertEquals(DefaultVals.PARTY_PERSON_DESC, model.getPartyTypeDescription());
  }

  @Test
  public void convertToModel_org() {
    BadgeEntity entity = getValidOrgBadgeEntity();
    BadgeSummaryConverter converter = new BadgeSummaryConverter();
    BadgeSummary model = converter.convertToModel(entity);

    // Check a few values.
    Assert.assertEquals(entity.getBadgeNo(), model.getBadgeNumber());
    Assert.assertEquals(entity.getBadgeStatus().name(), model.getStatusCode());
  }
}
