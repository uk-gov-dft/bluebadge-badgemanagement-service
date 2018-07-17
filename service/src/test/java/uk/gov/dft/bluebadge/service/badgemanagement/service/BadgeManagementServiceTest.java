package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestBase;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidateBadgeOrder;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidateCancelBadge;

public class BadgeManagementServiceTest extends BadgeTestBase {

  @Mock private BadgeManagementRepository repository;
  @Mock private ValidateBadgeOrder validateBadgeOrder;
  @Mock private ValidateCancelBadge validateCancelBadge;

  private BadgeManagementService service;

  @Before
  public void setUp() {
    service = new BadgeManagementService(repository, validateBadgeOrder, validateCancelBadge);
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
    BadgeEntity badgeEntity = getValidPersonBadgeEntity();
    when(repository.retrieveBadge(any())).thenReturn(badgeEntity);

    BadgeEntity result = service.retrieveBadge("ABC");
    Assert.assertEquals(badgeEntity, result);
  }

  @Test(expected = NotFoundException.class)
  public void retrieveBadge_notFound() {
    when(repository.retrieveBadge(any())).thenReturn(null);
    service.retrieveBadge("ABC");
  }

  @Test
  public void cancelBadge_ok() {
    CancelBadgeParams params =
        CancelBadgeParams.builder().cancelReasonCode("ABC").badgeNo("ABCABC").build();
    when(repository.cancelBadge(params)).thenReturn(1);
    service.cancelBadge(params);

    verify(validateCancelBadge, times(1)).validateRequest(params);
    verify(validateCancelBadge, never()).validateAfterFailedCancel(any());
    verify(repository, times(1)).cancelBadge(params);
  }

  @Test
  public void cancelBadge_failed() {
    CancelBadgeParams params =
        CancelBadgeParams.builder().cancelReasonCode("ABC").badgeNo("ABCABC").build();
    when(repository.cancelBadge(params)).thenReturn(0);
    service.cancelBadge(params);

    verify(validateCancelBadge, times(1)).validateRequest(params);
    verify(validateCancelBadge, times(1)).validateAfterFailedCancel(any());
    verify(repository, times(1)).cancelBadge(params);
  }
}
