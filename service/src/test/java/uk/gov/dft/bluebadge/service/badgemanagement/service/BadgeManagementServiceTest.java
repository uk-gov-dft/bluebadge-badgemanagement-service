package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.BeanUtils;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.common.security.model.LocalAuthority;
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
  private static final String LOCAL_AUTHORITY_SHORT_CODE = "ABERD";
  private static final LocalAuthority LOCAL_AUTHORITY =
      LocalAuthority.builder()
          .shortCode(LOCAL_AUTHORITY_SHORT_CODE)
          .build();

  @Mock private BadgeManagementRepository repositoryMock;
  @Mock private ValidateBadgeOrder validateBadgeOrderMock;
  @Mock private ValidateCancelBadge validateCancelBadgeMock;
  @Mock private SecurityUtils securityUtilsMock;

  private BadgeManagementService service;

  @Before
  public void setUp() {
    when(securityUtilsMock.getCurrentLocalAuthority()).thenReturn(LOCAL_AUTHORITY);
    service =
        new BadgeManagementService(
            repositoryMock, validateBadgeOrderMock, validateCancelBadgeMock, securityUtilsMock);
  }

  @Test
  public void createBadge() {
    BadgeEntity entity = getValidPersonBadgeEntity();
    entity.setNumberOfBadges(3);
    when(repositoryMock.retrieveNextBadgeNumber()).thenReturn(1234);
    List<String> result = service.createBadge(entity);

    // Then get 3 badges create with current user's local authority
    Assert.assertEquals(3, result.size());
    verify(repositoryMock, times(3)).createBadge(entity);
    verify(repositoryMock, times(3)).retrieveNextBadgeNumber();
    Assert.assertEquals("31E", result.get(1));
  }

  @Test
  public void createBadge_setLocalAuthorityToCurrentUsers() {
    BadgeEntity entity = getValidPersonBadgeEntity();
    entity.setLocalAuthorityShortCode("BIRM");
    entity.setNumberOfBadges(1);

    BadgeEntity expectedEntity = BadgeEntity.builder().build();
    BeanUtils.copyProperties(entity, expectedEntity);
    expectedEntity.setLocalAuthorityShortCode(LOCAL_AUTHORITY_SHORT_CODE);
    expectedEntity.setBadgeNo("31E");

    when(repositoryMock.retrieveNextBadgeNumber()).thenReturn(1234);
    List<String> results = service.createBadge(entity);

    // Then get 3 badges create with current user's local authority
    Assert.assertEquals(1, results.size());
    verify(repositoryMock, times(1)).createBadge(expectedEntity);
    verify(repositoryMock, times(1)).retrieveNextBadgeNumber();
    Assert.assertEquals("31E", results.get(0));
  }

  @Test
  public void findBadges_ok() {
    // Given search params valid when searching
    String name = "abc";
    FindBadgeParams params = FindBadgeParams.builder().name(name).build();
    // When searching
    service.findBadges(name, null);
    // Then search is done
    verify(repositoryMock, times(1)).findBadges(params);
  }

  @Test(expected = BadRequestException.class)
  public void findBadges_no_params() {
    // Given search params valid when searching
    String name = "  ";
    // When searching
    service.findBadges(name, null);
    // Then search is done
    verify(repositoryMock, never()).findBadges(any());
  }

  @Test(expected = BadRequestException.class)
  public void findBadges_too_many_params() {
    // Given search params valid when searching
    String name = "abc";
    String postcode = "def";
    // When searching
    service.findBadges(name, postcode);
    // Then search is done
    verify(repositoryMock, never()).findBadges(any());
  }

  @Test
  public void retrieveBadge_ok() {
    BadgeEntity badgeEntity = getValidPersonBadgeEntity();
    when(repositoryMock.retrieveBadge(any())).thenReturn(badgeEntity);

    BadgeEntity result = service.retrieveBadge("ABC");
    Assert.assertEquals(badgeEntity, result);
  }

  @Test(expected = NotFoundException.class)
  public void retrieveBadge_notFound() {
    when(repositoryMock.retrieveBadge(any())).thenReturn(null);
    service.retrieveBadge("ABC");
  }

  @Test
  public void cancelBadge_ok() {
    CancelBadgeParams params =
        CancelBadgeParams.builder().cancelReasonCode("ABC").badgeNo("ABCABC").build();
    when(repositoryMock.cancelBadge(params)).thenReturn(1);
    service.cancelBadge(params);

    verify(validateCancelBadgeMock, times(1)).validateRequest(params);
    verify(validateCancelBadgeMock, never()).validateAfterFailedCancel(any(), any());
    verify(repositoryMock, times(1)).cancelBadge(params);
  }

  @Test
  public void cancelBadge_failed() {
    CancelBadgeParams params =
        CancelBadgeParams.builder().cancelReasonCode("ABC").badgeNo("ABCABC").build();
    when(repositoryMock.cancelBadge(params)).thenReturn(0);
    service.cancelBadge(params);

    verify(validateCancelBadgeMock, times(1)).validateRequest(params);
    verify(validateCancelBadgeMock, times(1)).validateAfterFailedCancel(any(), any());
    verify(repositoryMock, times(1)).cancelBadge(params);
  }
}
