package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestBase;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

public class ReferenceDataServiceTest extends BadgeTestBase {

  @Test
  public void groupContainsKey() {
    Assert.assertTrue(
        ReferenceDataService.groupContainsKey(RefDataGroupEnum.GENDER, DefaultVals.GENDER_CODE));
    Assert.assertFalse(ReferenceDataService.groupContainsKey(RefDataGroupEnum.GENDER, "VOLVO"));
  }

  @Test
  public void getDescription() {
    assertEquals(
        DefaultVals.GENDER_DESC,
        ReferenceDataService.getDescription(RefDataGroupEnum.GENDER, DefaultVals.GENDER_CODE));
  }
}
