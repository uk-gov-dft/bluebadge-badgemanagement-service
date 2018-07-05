package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestBase;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;

public class BadgeManagementServiceTest extends BadgeTestBase {

  @Mock private BadgeManagementRepository repository;

  private BadgeManagementService service;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    service = new BadgeManagementService(repository);
  }

  @Test
  public void createBadge() {
    BadgeEntity entity = getValidBadge();
    entity.setNumberOfBadges(3);
    when(repository.retrieveNextBadgeNumber()).thenReturn(1234);
    List<String> result = service.createBadge(entity);

    // Then get 3 badges create.
    Assert.assertEquals(3, result.size());
    verify(repository, times(3)).createBadge(entity);
    verify(repository, times(3)).retrieveNextBadgeNumber();
    Assert.assertEquals("31E", result.get(1));
  }
}
