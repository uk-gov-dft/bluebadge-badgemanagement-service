package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableSet;
import java.time.LocalDate;
import java.time.Period;
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
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.DeleteBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.ReplaceBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.BlacklistedCombinationsFilter;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidateBadgeOrder;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidateCancelBadge;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidateReplaceBadge;

public class BadgeManagementServiceTest extends BadgeTestBase {
  private static final String LOCAL_AUTHORITY_SHORT_CODE = "ABERD";

  @Mock private BadgeManagementRepository repositoryMock;
  @Mock private ValidateBadgeOrder validateBadgeOrderMock;
  @Mock private ValidateCancelBadge validateCancelBadgeMock;
  @Mock private ValidateReplaceBadge validateReplaceBadgeMock;
  @Mock private SecurityUtils securityUtilsMock;
  @Mock private PhotoService photoServiceMock;
  @Mock private BlacklistedCombinationsFilter blacklistFilter;
  @Mock private BadgeNumberService numberService;

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
            validateReplaceBadgeMock,
            securityUtilsMock,
            photoServiceMock,
            numberService,
            blacklistFilter);
  }

  @Test
  public void createBadge() {
    BadgeOrderRequest model = getValidBadgeOrderPersonRequest();
    model.setNumberOfBadges(3);
    when(numberService.getBagdeNumber()).thenReturn(2345);
    when(blacklistFilter.isValid(any(String.class))).thenReturn(true);
    List<String> result = service.createBadge(model);

    // Then get 3 badges create with current user's local authority
    Assert.assertEquals(3, result.size());
    verify(repositoryMock, times(3)).createBadge(any(BadgeEntity.class));
    verify(numberService, times(3)).getBagdeNumber();
    Assert.assertEquals("2227L7", result.get(1));
  }

  @Test
  public void createBadge_setLocalAuthorityToCurrentUsers() {
    BadgeOrderRequest model = getValidBadgeOrderPersonRequest();
    model.setNumberOfBadges(1);
    model.setImageFile("B64IMAGE");
    BadgeEntity entity = new BadgeOrderRequestConverter().convertToEntity(model);
    entity.setLocalAuthorityShortCode(LOCAL_AUTHORITY_SHORT_CODE);
    entity.setBadgeNo("2227L7");

    S3KeyNames names = new S3KeyNames();
    names.setThumbnailKeyName("thumb");
    names.setOriginalKeyName("orig");

    entity.setImageLinkOriginal("orig");
    entity.setImageLink("thumb");

    when(numberService.getBagdeNumber()).thenReturn(2345);
    when(photoServiceMock.photoUpload(any(), any())).thenReturn(names);
    when(blacklistFilter.isValid(any(String.class))).thenReturn(true);
    List<String> results = service.createBadge(model);

    Assert.assertEquals(1, results.size());
    verify(repositoryMock, times(1)).createBadge(entity);
    verify(numberService, times(1)).getBagdeNumber();
    Assert.assertEquals("2227L7", results.get(0));
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

  @Test(expected = NotFoundException.class)
  public void replaceBadge_notFound() {
    String badgeNo = "BADGENO";
    ReplaceBadgeParams params = replaceParams(badgeNo, "HOME", "FAST", "LOST");

    when(repositoryMock.retrieveBadge(any())).thenReturn(null);
    service.replaceBadge(params);
  }

  @Test(expected = NotFoundException.class)
  public void replaceBadge_alreadyDeleted() {
    String badgeNo = "BADGENO";
    ReplaceBadgeParams params = replaceParams(badgeNo, "HOME", "FAST", "LOST");

    BadgeEntity badge =
        BadgeEntity.builder().badgeNo(badgeNo).badgeStatus(BadgeEntity.Status.DELETED).build();

    when(repositoryMock.retrieveBadge(any())).thenReturn(badge);
    service.replaceBadge(params);
  }

  @Test(expected = BadRequestException.class)
  public void replaceBadge_alreadyExpired() {
    String badgeNo = "BADGENO";
    ReplaceBadgeParams params = replaceParams(badgeNo, "HOME", "FAST", "LOST");

    BadgeEntity badge =
        BadgeEntity.builder()
            .badgeNo(badgeNo)
            .badgeStatus(BadgeEntity.Status.ISSUED)
            .expiryDate(LocalDate.now().minus(Period.ofDays(1)))
            .build();

    when(repositoryMock.retrieveBadge(any())).thenReturn(badge);
    service.replaceBadge(params);
  }

  @Test(expected = BadRequestException.class)
  public void replaceBadge_alreadyCancelledOrReplaced() {
    String badgeNo = "BADGENO";
    ReplaceBadgeParams params = replaceParams(badgeNo, "HOME", "FAST", "LOST");

    BadgeEntity badge =
        BadgeEntity.builder()
            .badgeNo(badgeNo)
            .badgeStatus(BadgeEntity.Status.REPLACED)
            .expiryDate(LocalDate.now().plus(Period.ofDays(1)))
            .build();

    when(repositoryMock.retrieveBadge(any())).thenReturn(badge);
    service.replaceBadge(params);
  }

  @Test
  public void replaceBadge_ok() {
    String badgeNo = "BAD234";
    String newBadgeNo = "BAD567";
    ReplaceBadgeParams params = replaceParams(badgeNo, "HOME", "FAST", "DAMAGED");

    BadgeEntity badge =
        BadgeEntity.builder()
            .badgeNo(badgeNo)
            .badgeStatus(BadgeEntity.Status.ISSUED)
            .expiryDate(LocalDate.now().plus(Period.ofDays(1)))
            .build();

    when(repositoryMock.retrieveBadge(any())).thenReturn(badge);
    when(numberService.getBagdeNumber()).thenReturn(30169285);
    when(blacklistFilter.isValid(any())).thenReturn(true);
    String result = service.replaceBadge(params);
    assertEquals(newBadgeNo, result);
    verify(repositoryMock, times(1)).retrieveBadge(any());
    verify(repositoryMock, times(1)).replaceBadge(any());
    verify(repositoryMock, times(1)).createBadge(any());
    verify(numberService, times(1)).getBagdeNumber();
    verify(blacklistFilter, times(1)).isValid(any());
  }

  private ReplaceBadgeParams replaceParams(
      String badgeNo, String deliveryCode, String deliveryOption, String reason) {
    return ReplaceBadgeParams.builder()
        .badgeNumber(badgeNo)
        .deliveryCode(deliveryCode)
        .deliveryOptionCode(deliveryOption)
        .reasonCode(reason)
        .startDate(LocalDate.now())
        .status(Status.REPLACED)
        .build();
  }
}
