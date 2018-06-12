package uk.gov.dft.bluebadge.client.badgemanagement.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.model.badgemanagement.Badge;
import uk.gov.dft.bluebadge.model.badgemanagement.BadgeSummary;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BadgeManagementApiClientTest {

  @Autowired private BadgeManagementApiClient badgeManagementApiClient;

  @Test
  public void findBadges() {
    List<BadgeSummary> response = badgeManagementApiClient.findBadges("", "", "");
    Assert.notNull(response, "");
  }

  @Test
  public void retrieveBadge() {
    Badge response = badgeManagementApiClient.retrieveBadge("");
    Assert.notNull(response, "");
  }
}
