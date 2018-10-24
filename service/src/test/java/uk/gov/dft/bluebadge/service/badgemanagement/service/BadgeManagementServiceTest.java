package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestBase;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.BadgeOrderRequestConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.DeleteBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidateBadgeOrder;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidateCancelBadge;

public class BadgeManagementServiceTest extends BadgeTestBase {
  private static final String LOCAL_AUTHORITY_SHORT_CODE = "ABERD";

  @Mock private BadgeManagementRepository repositoryMock;
  @Mock private ValidateBadgeOrder validateBadgeOrderMock;
  @Mock private ValidateCancelBadge validateCancelBadgeMock;
  @Mock private SecurityUtils securityUtilsMock;
  @Mock private PhotoService photoServiceMock;

  private BadgeManagementService service;

  @Before
  public void setUp() {
    when(securityUtilsMock.getCurrentLocalAuthorityShortCode())
        .thenReturn(LOCAL_AUTHORITY_SHORT_CODE);
    service =
        new BadgeManagementService(
            repositoryMock,
            validateBadgeOrderMock,
            validateCancelBadgeMock,
            securityUtilsMock,
            photoServiceMock);
  }

  @Test
  public void createBadge() {
    BadgeOrderRequest model = getValidBadgeOrderPersonRequest();
    model.setNumberOfBadges(3);
    when(repositoryMock.retrieveNextBadgeNumber()).thenReturn(1234);
    List<String> result = service.createBadge(model);

    // Then get 3 badges create with current user's local authority
    Assert.assertEquals(3, result.size());
    verify(repositoryMock, times(3)).createBadge(any(BadgeEntity.class));
    verify(repositoryMock, times(3)).retrieveNextBadgeNumber();
    Assert.assertEquals("31E", result.get(1));
  }

  @Test
  public void createBadge_setLocalAuthorityToCurrentUsers() {
    BadgeOrderRequest model = getValidBadgeOrderPersonRequest();
    model.setNumberOfBadges(1);
    model.setImageFile("B64IMAGE");
    BadgeEntity entity = new BadgeOrderRequestConverter().convertToEntity(model);
    entity.setLocalAuthorityShortCode(LOCAL_AUTHORITY_SHORT_CODE);
    entity.setBadgeNo("31E");

    S3KeyNames names = new S3KeyNames();
    names.setThumbnailKeyName("thumb");
    names.setOriginalKeyName("orig");

    entity.setImageLinkOriginal("orig");
    entity.setImageLink("thumb");

    when(repositoryMock.retrieveNextBadgeNumber()).thenReturn(1234);
    when(photoServiceMock.photoUpload(any(), any())).thenReturn(names);
    List<String> results = service.createBadge(model);

    Assert.assertEquals(1, results.size());
    verify(repositoryMock, times(1)).createBadge(entity);
    verify(repositoryMock, times(1)).retrieveNextBadgeNumber();
    Assert.assertEquals("31E", results.get(0));
  }

  @Test
  public void findBadges_ok() {
    // Given search params valid when searching
    String name = "abc";
    Set<String> statuses =
        ImmutableSet.of(
            BadgeEntity.Status.ISSUED.name(),
            BadgeEntity.Status.CANCELLED.name(),
            BadgeEntity.Status.REPLACED.name());
    FindBadgeParams params = FindBadgeParams.builder().name(name).statuses(statuses).build();
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
    verify(validateCancelBadgeMock, never()).validateAfterFailedCancel(any());
    verify(repositoryMock, times(1)).cancelBadge(params);
  }

  @Test
  public void cancelBadge_failed() {
    CancelBadgeParams params =
        CancelBadgeParams.builder().cancelReasonCode("ABC").badgeNo("ABCABC").build();
    when(repositoryMock.cancelBadge(params)).thenReturn(0);
    service.cancelBadge(params);

    verify(validateCancelBadgeMock, times(1)).validateRequest(params);
    verify(validateCancelBadgeMock, times(1)).validateAfterFailedCancel(any());
    verify(repositoryMock, times(1)).cancelBadge(params);
  }

  @Test
  public void deleteBadge_ok_noImages() {
    BadgeEntity badge = BadgeEntity.builder().badgeNo("BADGENO").build();
    when(repositoryMock.retrieveBadge(any())).thenReturn(badge);
    DeleteBadgeParams deleteBadgeParams =
        DeleteBadgeParams.builder()
            .deleteStatus(BadgeEntity.Status.DELETED)
            .badgeNo("BADGENO")
            .build();

    service.deleteBadge("BADGENO");

    verify(repositoryMock, times(1)).deleteBadge(deleteBadgeParams);
    verify(photoServiceMock, never()).deletePhoto(any(), any());
  }

  @Test
  public void deleteBadge_ok_imagesAlsoDeleted() {
    BadgeEntity badge =
        BadgeEntity.builder()
            .badgeNo("BADGENO")
            .imageLink("image1")
            .imageLinkOriginal("image2")
            .build();
    when(repositoryMock.retrieveBadge(any())).thenReturn(badge);
    DeleteBadgeParams deleteBadgeParams =
        DeleteBadgeParams.builder()
            .deleteStatus(BadgeEntity.Status.DELETED)
            .badgeNo("BADGENO")
            .build();

    service.deleteBadge("BADGENO");

    verify(repositoryMock, times(1)).deleteBadge(deleteBadgeParams);
    verify(photoServiceMock, times(1)).deletePhoto("BADGENO", "image1");
    verify(photoServiceMock, times(1)).deletePhoto("BADGENO", "image2");
  }

  @Test(expected = NotFoundException.class)
  public void deleteBadge_whenRepositoryReturnedNull() {
    service.deleteBadge("BADGENO");
  }

  @Test(expected = NotFoundException.class)
  public void deleteBadge_alreadyDeleted() {
    BadgeEntity badge =
        BadgeEntity.builder().badgeNo("BADGENO").badgeStatus(BadgeEntity.Status.DELETED).build();
    when(repositoryMock.retrieveBadge(any())).thenReturn(badge);
    service.deleteBadge("BADGENO");
  }
}
