package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestBase;

public class RefDataTest extends BadgeTestBase {

  @Test
  public void groupContainsKey() {
    Assert.assertTrue(RefData.groupContainsKey(RefDataGroupEnum.GENDER, DefaultVals.GENDER_CODE));
    Assert.assertFalse(RefData.groupContainsKey(RefDataGroupEnum.GENDER, "VOLVO"));
  }

  @Test
  public void getDescription() {
    assertEquals(
        DefaultVals.GENDER_DESC,
        RefData.getDescription(RefDataGroupEnum.GENDER, DefaultVals.GENDER_CODE));
  }
}
