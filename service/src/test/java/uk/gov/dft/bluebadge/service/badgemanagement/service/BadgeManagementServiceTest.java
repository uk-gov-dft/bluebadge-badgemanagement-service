package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.getValidBadgeOrderPersonRequest;
import static uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestFixture.getValidPersonBadgeEntity;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status.CANCELLED;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status.ISSUED;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status.ORDERED;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status.PROCESSED;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status.REJECT;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status.REPLACED;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.service.badgemanagement.model.BadgeOrderRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.controller.PagingParams;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.BadgeOrderRequestConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.BadgeSummaryConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeZipEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.DeleteBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.ReplaceBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.service.audit.BadgeAuditLogger;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.BlacklistedCombinationsFilter;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidateBadgeOrder;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidateCancelBadge;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidateReplaceBadge;

public class BadgeManagementServiceTest {
  private static final String LOCAL_AUTHORITY_SHORT_CODE = "ABERD";

  @Mock private BadgeManagementRepository repositoryMock;
  @Mock private ValidateBadgeOrder validateBadgeOrderMock;
  @Mock private ValidateCancelBadge validateCancelBadgeMock;
  @Mock private ValidateReplaceBadge validateReplaceBadgeMock;
  @Mock private SecurityUtils securityUtilsMock;
  @Mock private PhotoService photoServiceMock;
  @Mock private BlacklistedCombinationsFilter blacklistFilter;
  @Mock private BadgeNumberService numberService;
  @Mock private BadgeAuditLogger badgeAuditLogger;
  @Mock private BadgeSummaryConverter badgeSummaryConverterMock;

  private BadgeManagementService service;

  public BadgeManagementServiceTest() {
    MockitoAnnotations.initMocks(this);
  }

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
            blacklistFilter,
            badgeAuditLogger,
            badgeSummaryConverterMock);
  }

  @Test
  public void createBadges() {
    BadgeOrderRequest model = getValidBadgeOrderPersonRequest();
    model.setNumberOfBadges(3);
    when(numberService.getBagdeNumber()).thenReturn(2345);
    when(blacklistFilter.isValid(any(String.class))).thenReturn(true);
    List<String> result = service.createBadges(model);

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
    entity.setBadgeHash(BadgeHashService.getBadgeEntityHash(entity));

    when(numberService.getBagdeNumber()).thenReturn(2345);
    when(photoServiceMock.photoUpload(any(), any())).thenReturn(names);
    when(blacklistFilter.isValid(any(String.class))).thenReturn(true);
    List<String> results = service.createBadges(model);

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
            ISSUED.name(),
            CANCELLED.name(),
            REPLACED.name(),
            PROCESSED.name(),
            REJECT.name(),
            ORDERED.name());
    FindBadgeParams params = FindBadgeParams.builder().name(name).statuses(statuses).build();
    PagingParams pagingParams = new PagingParams();
    pagingParams.setPageNum(1);
    pagingParams.setPageSize(50);
    // When searching
    service.findBadges(name, null, pagingParams);
    // Then search is done
    verify(repositoryMock, times(1)).findBadges(params, 1, 50);
    verify(badgeSummaryConverterMock, times(1)).convertToModelList(any());
  }

  @Test(expected = BadRequestException.class)
  public void findBadges_no_params() {
    // Given search params valid when searching
    String name = "  ";
    // When searching
    PagingParams pagingParams = new PagingParams();
    pagingParams.setPageNum(1);
    pagingParams.setPageSize(50);
    service.findBadges(name, null, pagingParams);
    // Then search is done
    verify(repositoryMock, never()).findBadges(any(), any(), any());
    verify(badgeSummaryConverterMock, never()).convertToModelList(any());
  }

  @Test(expected = BadRequestException.class)
  public void findBadges_too_many_params() {
    // Given search params valid when searching
    String name = "abc";
    String postcode = "def";
    // When searching
    PagingParams pagingParams = new PagingParams();
    pagingParams.setPageNum(1);
    pagingParams.setPageSize(50);
    service.findBadges(name, postcode, pagingParams);
    // Then search is done
    verify(repositoryMock, never()).findBadges(any(), eq(1), eq(50));
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
            .badgeStatus(ISSUED)
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
            .badgeStatus(REPLACED)
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
            .badgeStatus(ISSUED)
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
        .status(REPLACED)
        .build();
  }

  @Test
  public void checkBadgeHashUnique_Test() {
    when(repositoryMock.findBadgeHash(any())).thenReturn(null);
    service.checkBadgeHashUnique(BadgeEntity.builder().build());

    try {
      when(repositoryMock.findBadgeHash(any())).thenReturn(Lists.newArrayList("123456"));
      service.checkBadgeHashUnique(BadgeEntity.builder().build());
      fail("Should have had bad request exception.");
    } catch (BadRequestException e) {
      assertThat(e.getResponse().getBody().getError().getMessage())
          .isEqualTo("AlreadyExists.badge");
    }
  }

  @Test
  @SneakyThrows
  public void retrieveBadgesByLa() {
    BadgeZipEntity badge = BadgeZipEntity.builder().badgeNo("123456").build();
    when(repositoryMock.retrieveBadgesByLa(any())).thenReturn(ImmutableList.of(badge));

    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    service.retrieveBadgesByLa(stream, "ABCD");
    ZipInputStream zipInputStream =
        new ZipInputStream(new ByteArrayInputStream(stream.toByteArray()));
    ZipEntry entry = zipInputStream.getNextEntry();

    assertThat(entry.getName())
        .isEqualTo(LocalDate.now().format(DateTimeFormatter.ISO_DATE) + "_ABCD.csv");
    Scanner sc = new Scanner(zipInputStream);
    int i = 0;
    while (sc.hasNextLine()) {
      // Should have header line.
      if (i == 0) {
        assertThat(sc.nextLine())
            .isEqualTo(
                "badge_no,badge_status,party_code,\"local_authority_short_code\"," +
                    "local_authority_ref,app_date,app_channel_code,start_date," +
                    "expiry_date,eligibility_code,deliver_to_code,deliver_option_code," +
                    "cancel_reason_code,replace_reason_code,order_date,rejected_reason," +
                    "rejected_date_time,issued_date_time,print_request_date_time," +
                    "not_for_reassessment"
            );
      }
      // Should have line for badge.
      if (i == 1) {
        assertThat(sc.nextLine()).contains("123456");
      }
      i++;
    }
    // Should not have any more lines.
    assertThat(i).isEqualTo(2);
  }

  @Test
  @SneakyThrows
  public void retrieveBadgesByLa_noResults() {
    when(repositoryMock.retrieveBadgesByLa(any())).thenReturn(ImmutableList.of());
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    service.retrieveBadgesByLa(stream, "ABCD");
    ZipInputStream zipInputStream =
        new ZipInputStream(new ByteArrayInputStream(stream.toByteArray()));
    ZipEntry entry = zipInputStream.getNextEntry();

    assertThat(entry.getName())
        .isEqualTo(LocalDate.now().format(DateTimeFormatter.ISO_DATE) + "_ABCD.csv");
    Scanner sc = new Scanner(zipInputStream);
    int i = 0;
    while (sc.hasNextLine()) {
      // Should have header line.
      assertThat(sc.nextLine())
          .isEqualTo(
              "badge_no,badge_status,party_code,\"local_authority_short_code\"," +
                  "local_authority_ref,app_date,app_channel_code,start_date,expiry_date," +
                  "eligibility_code,deliver_to_code,deliver_option_code,cancel_reason_code," +
                  "replace_reason_code,order_date,rejected_reason,rejected_date_time," +
                  "issued_date_time,print_request_date_time,not_for_reassessment"
          );
      i++;
    }
    // Should not have any more lines.
    assertThat(i).isEqualTo(1);
  }
}
