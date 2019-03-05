package uk.gov.dft.bluebadge.service.badgemanagement.service;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

public class ReferenceDataServiceTest {

  @Test
  public void groupContainsKey() {
    ReferenceDataService service =
        new ReferenceDataService(BadgeTestFixture.getMockRefDataApiClient());
    Assert.assertTrue(
        service.groupContainsKey(
            RefDataGroupEnum.GENDER, BadgeTestFixture.DefaultVals.GENDER_CODE));
    Assert.assertFalse(service.groupContainsKey(RefDataGroupEnum.GENDER, "VOLVO"));
  }
}
