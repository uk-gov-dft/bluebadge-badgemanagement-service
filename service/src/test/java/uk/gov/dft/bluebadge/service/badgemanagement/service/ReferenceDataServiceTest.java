package uk.gov.dft.bluebadge.service.badgemanagement.service;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestBase;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum;

public class ReferenceDataServiceTest extends BadgeTestBase {

  @Test
  public void groupContainsKey() {
    Assert.assertTrue(
        referenceDataService.groupContainsKey(RefDataGroupEnum.GENDER, DefaultVals.GENDER_CODE));
    Assert.assertFalse(referenceDataService.groupContainsKey(RefDataGroupEnum.GENDER, "VOLVO"));
  }
}
