package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestBase;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.service.badgemanagement.service.exception.NotFoundException;

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
    BadgeEntity entity = getValidPersonBadgeEntity();
    entity.setNumberOfBadges(3);
    when(repository.retrieveNextBadgeNumber()).thenReturn(1234);
    List<String> result = service.createBadge(entity);

    // Then get 3 badges create.
    Assert.assertEquals(3, result.size());
    verify(repository, times(3)).createBadge(entity);
    verify(repository, times(3)).retrieveNextBadgeNumber();
    Assert.assertEquals("31E", result.get(1));
  }

  @Test
  public void findBadges_ok() {
    // Given search params valid when searching
    String name = "abc";
    FindBadgeParams params = FindBadgeParams.builder().name(name).build();
    // When searching
    service.findBadges(name, null);
    // Then search is done
    verify(repository, times(1)).findBadges(params);
  }

  @Test(expected = BadRequestException.class)
  public void findBadges_no_params() {
    // Given search params valid when searching
    String name = "  ";
    // When searching
    service.findBadges(name, null);
    // Then search is done
    verify(repository, never()).findBadges(any());
  }

  @Test(expected = BadRequestException.class)
  public void findBadges_too_many_params() {
    // Given search params valid when searching
    String name = "abc";
    String postcode = "def";
    // When searching
    service.findBadges(name, postcode);
    // Then search is done
    verify(repository, never()).findBadges(any());
  }

  @Test
  public void retrieveBadge_ok() {
    when(repository.retrieveBadge(any())).thenReturn(getValidPersonBadgeEntity());

    BadgeEntity result = service.retrieveBadge("ABC");
    Assert.assertEquals(getValidPersonBadgeEntity(), result);
  }

  @Test(expected = NotFoundException.class)
  public void retrieveBadge_notFound() {
    when(repository.retrieveBadge(any())).thenReturn(null);
    service.retrieveBadge("ABC");
  }
}
