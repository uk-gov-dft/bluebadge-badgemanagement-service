package uk.gov.dft.bluebadge.client.badgemanagement.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.model.badgemanagement.BadgeResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.BadgesResponse;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BadgeManagementServiceTest {

  @Autowired private BadgeManagementService badgeManagementService;

  @Test
  public void searchBadges() {
    BadgesResponse response = badgeManagementService.searchBadges("", "", "");
    Assert.notNull(response.getData(), "");
  }

  @Test
  public void retrieveBadge() {
    BadgeResponse response = badgeManagementService.retrieveBadge("");
    Assert.notNull(response.getData(), "");
  }
}
