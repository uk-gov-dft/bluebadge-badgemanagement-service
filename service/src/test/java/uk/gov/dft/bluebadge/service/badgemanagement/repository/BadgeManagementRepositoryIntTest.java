package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status.ISSUED;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status.REJECT;

import com.google.common.collect.ImmutableSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.common.service.enums.EligibilityType;
import uk.gov.dft.bluebadge.service.badgemanagement.ApplicationContextTests;
import uk.gov.dft.bluebadge.service.badgemanagement.model.CancelReason;
import uk.gov.dft.bluebadge.service.badgemanagement.model.DeliverOption;
import uk.gov.dft.bluebadge.service.badgemanagement.model.DeliverTo;
import uk.gov.dft.bluebadge.service.badgemanagement.model.ReplaceReason;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeZipEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.DeleteBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgesForPrintBatchParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.ReplaceBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.RetrieveBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.UpdateBadgeStatusParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.UpdateBadgesStatusesForBatchParams;

@RunWith(SpringRunner.class)
@Transactional
public class BadgeManagementRepositoryIntTest extends ApplicationContextTests {

  private static final DateTimeFormatter DATE_TIME_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @SuppressWarnings("unused")
  @Autowired
  private BadgeManagementRepository badgeManagementRepository;

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void retrieveBadge_ok() {
    RetrieveBadgeParams retrieveParams = RetrieveBadgeParams.builder().badgeNo("KKKKKK").build();
    BadgeEntity badgeEntity = badgeManagementRepository.retrieveBadge(retrieveParams);
    assertThat(badgeEntity).isNotNull();

    assertThat(badgeEntity.getBadgeNo()).isEqualTo("KKKKKK");
    assertThat(badgeEntity.getBadgeStatus()).isEqualTo(ISSUED);

    assertThat(badgeEntity.getImageLink()).isEqualTo("badge/KKKKKK/thumbnail.jpg");
    assertThat(badgeEntity.getImageLinkOriginal()).isEqualTo("badge/KKKKKK/original.jpg");
    assertThat(badgeEntity.getNotForReassessment()).isEqualTo(true);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void retrieveBadge_shouldReturnNullForNotForReassessment() {
    RetrieveBadgeParams retrieveParams = RetrieveBadgeParams.builder().badgeNo("KKKKKD").build();
    BadgeEntity badgeEntity = badgeManagementRepository.retrieveBadge(retrieveParams);
    assertThat(badgeEntity).isNotNull();

    assertThat(badgeEntity.getNotForReassessment()).isEqualTo(null);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void retrieveBadge_shouldReturnFalseForNotForReassessment() {
    RetrieveBadgeParams retrieveParams = RetrieveBadgeParams.builder().badgeNo("KKKKKC").build();
    BadgeEntity badgeEntity = badgeManagementRepository.retrieveBadge(retrieveParams);
    assertThat(badgeEntity).isNotNull();

    assertThat(badgeEntity.getNotForReassessment()).isEqualTo(false);
  }

  /**
   * Should never be multiple issued batch_badge records, but if there are then need to still
   * retrieve badge. This happened once due to a print confirmation file being processed twice. The
   * retrieve badge query has nested selects. These returned multiple values - breaking sql.
   * Grouping added to nested selects to defend against.
   */
  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void retrieveBadge_multipleConfirmtionBatches_ok() {
    RetrieveBadgeParams retrieveParams = RetrieveBadgeParams.builder().badgeNo("DUPES1").build();
    BadgeEntity badgeEntity = badgeManagementRepository.retrieveBadge(retrieveParams);
    assertThat(badgeEntity).isNotNull();

    assertThat(badgeEntity.getBadgeNo()).isEqualTo("DUPES1");
    assertThat(badgeEntity.getIssuedDate()).isEqualTo("2019-03-07T01:03");
    assertThat(badgeEntity.getBadgeStatus()).isEqualTo(ISSUED);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void retrieveBadge_multipleRejectBatches_ok() {
    RetrieveBadgeParams retrieveParams = RetrieveBadgeParams.builder().badgeNo("DUPES2").build();
    BadgeEntity badgeEntity = badgeManagementRepository.retrieveBadge(retrieveParams);
    assertThat(badgeEntity).isNotNull();

    assertThat(badgeEntity.getBadgeNo()).isEqualTo("DUPES2");
    assertThat(badgeEntity.getIssuedDate()).isNull();
    assertThat(badgeEntity.getRejectedReason()).isEqualTo("help");
    assertThat(badgeEntity.getPrintRequestDateTime()).isEqualTo("2011-01-01T03:00");
    assertThat(badgeEntity.getBadgeStatus()).isEqualTo(REJECT);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void
      retrieveBadge_shouldReturnDetailsAndSentPrinterDate_WhenBadgeWasSentToPrinterProvider() {
    RetrieveBadgeParams retrieveParams = RetrieveBadgeParams.builder().badgeNo("NNNJMJ").build();
    BadgeEntity badgeEntity = badgeManagementRepository.retrieveBadge(retrieveParams);
    assertThat(badgeEntity).isNotNull();

    assertThat(badgeEntity.getBadgeNo()).isEqualTo("NNNJMJ");
    assertThat(badgeEntity.getBadgeStatus()).isEqualTo(BadgeEntity.Status.PROCESSED);
    assertThat(badgeEntity.getPrintRequestDateTime())
        .isEqualTo(LocalDateTime.parse("2019-03-07 01:01:00", DATE_TIME_FORMAT));
    assertThat(badgeEntity.getIssuedDate()).isNull();
    assertThat(badgeEntity.getRejectedReason()).isNull();
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void
      retrieveBadge_shouldReturnDetailsAndSentPrinterDateAndIssuedDate_WhenBadgeWasIssued() {
    RetrieveBadgeParams retrieveParams = RetrieveBadgeParams.builder().badgeNo("NNNJMH").build();
    BadgeEntity badgeEntity = badgeManagementRepository.retrieveBadge(retrieveParams);
    assertThat(badgeEntity).isNotNull();

    assertThat(badgeEntity.getBadgeNo()).isEqualTo("NNNJMH");
    assertThat(badgeEntity.getBadgeStatus()).isEqualTo(ISSUED);
    assertThat(badgeEntity.getPrintRequestDateTime())
        .isEqualTo(LocalDateTime.parse("2019-03-07 01:01:00", DATE_TIME_FORMAT));
    assertThat(badgeEntity.getIssuedDate())
        .isEqualTo(LocalDateTime.parse("2019-03-07 01:02:00", DATE_TIME_FORMAT));
    assertThat(badgeEntity.getRejectedReason()).isNull();
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void
      retrieveBadge_shouldReturnDetailsAndSentPrinterDateAndRejectedReason_WhenBadgeWasRejected() {
    RetrieveBadgeParams retrieveParams = RetrieveBadgeParams.builder().badgeNo("NNNJMF").build();
    BadgeEntity badgeEntity = badgeManagementRepository.retrieveBadge(retrieveParams);
    assertThat(badgeEntity).isNotNull();

    assertThat(badgeEntity.getBadgeNo()).isEqualTo("NNNJMF");
    assertThat(badgeEntity.getBadgeStatus()).isEqualTo(REJECT);
    assertThat(badgeEntity.getPrintRequestDateTime())
        .isEqualTo(LocalDateTime.parse("2019-03-07 01:03:00", DATE_TIME_FORMAT));
    assertThat(badgeEntity.getIssuedDate()).isNull();
    assertThat(badgeEntity.getRejectedReason()).isEqualTo("my rejected reason");
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void cancelBadge_ok() {
    CancelBadgeParams params =
        CancelBadgeParams.builder()
            .badgeNo("KKKKKK")
            .cancelReasonCode(CancelReason.REVOKE)
            .localAuthorityShortCode("ABERD")
            .build();
    int recordsAffected = badgeManagementRepository.cancelBadge(params);
    assertThat(recordsAffected).isEqualTo(1);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void cancelBadge_badgeNotExist() {
    CancelBadgeParams params =
        CancelBadgeParams.builder()
            .badgeNo("NOTEXI")
            .cancelReasonCode(CancelReason.REVOKE)
            .localAuthorityShortCode("ABERD")
            .build();
    int recordsAffected = badgeManagementRepository.cancelBadge(params);
    assertThat(recordsAffected).isEqualTo(0);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void cancelBadge_existsInDifferentLocalAuthority() {
    CancelBadgeParams params =
        CancelBadgeParams.builder()
            .badgeNo("KKKKKK")
            .cancelReasonCode(CancelReason.REVOKE)
            .localAuthorityShortCode("ANGL")
            .build();
    int recordsAffected = badgeManagementRepository.cancelBadge(params);
    assertThat(recordsAffected).isEqualTo(0);
  }

  @Test
  public void findBadges_ShouldReturn50FirstResultsOrderedByStartDateDescendingOrder() {
    final int FIRST_ID = 100;
    final int LAST_ID = FIRST_ID + 100;
    final int RESULTS_LIMIT = 50;
    final LocalDate INITIAL_START_DATE = LocalDate.now().minusDays(300);

    List<BadgeEntity> badgeEntityList = Lists.newArrayList();
    for (int id = LAST_ID; id > FIRST_ID; id--) {
      BadgeEntity badgeEntity =
          BadgeEntity.builder()
              .badgeNo(String.valueOf(id))
              .badgeStatus(ISSUED)
              .contactName("ZZZZ" + id)
              .partyCode("PAR")
              .localAuthorityShortCode("WINMD")
              .appDate(LocalDate.now())
              .appChannelCode("APC")
              .startDate(INITIAL_START_DATE.plusDays(id))
              .expiryDate(INITIAL_START_DATE.plusDays(id).plusYears(2))
              .deliverToCode("DE")
              .deliverOptionCode("DOPT")
              .holderName("ZZZZ" + id)
              .contactBuildingStreet("building" + id)
              .contactTownCity("town" + id)
              .contactPostcode("CPC111")
              .primaryPhoneNo("01478523698")
              .orderDate(LocalDate.now())
              .build();
      badgeManagementRepository.createBadge(badgeEntity);
      badgeEntityList.add(badgeEntity);
    }

    FindBadgeParams params = FindBadgeParams.builder().name("%ZZZZ%").build();
    List<BadgeEntity> badges = badgeManagementRepository.findBadges(params, 1, 50);

    badgeEntityList.sort((b1, b2) -> b2.getStartDate().compareTo(b1.getStartDate()));
    List<BadgeEntity> expectedBadgeEntityList =
        badgeEntityList
            .stream()
            .limit(RESULTS_LIMIT)
            .peek(b -> b.setOrderDate(null))
            .collect(Collectors.toList());

    assertThat(badges).hasSize(RESULTS_LIMIT);
    assertThat(badges).isEqualTo(expectedBadgeEntityList);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void findBadges_shouldSearchByStatus() {
    Set<String> statuses = ImmutableSet.of(ISSUED.name());
    FindBadgeParams params = FindBadgeParams.builder().statuses(statuses).build();
    List<BadgeEntity> badges = badgeManagementRepository.findBadges(params, 1, 50);
    assertThat(badges).isNotEmpty();
    assertThat(badges).extracting("badgeStatus").containsOnly(ISSUED);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void findBadges_shouldSearchByStatus_deleted() {
    Set<String> statuses = ImmutableSet.of(BadgeEntity.Status.DELETED.name());
    FindBadgeParams params = FindBadgeParams.builder().statuses(statuses).build();
    List<BadgeEntity> badges = badgeManagementRepository.findBadges(params, 1, 50);
    assertThat(badges).isNotEmpty();
    assertThat(badges).extracting("badgeStatus").containsOnly(BadgeEntity.Status.DELETED);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void findBadges_shouldSearchByStatusAndPostCode() {
    Set<String> statuses = ImmutableSet.of(ISSUED.name());
    FindBadgeParams params =
        FindBadgeParams.builder().postcode("S637FU").statuses(statuses).build();
    List<BadgeEntity> badges = badgeManagementRepository.findBadges(params, 1, 50);
    assertThat(badges).isNotEmpty();
    assertThat(badges).extracting("badgeStatus").containsOnly(ISSUED);
    assertThat(badges).extracting("contactPostcode").containsOnly("S637FU");
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void findBadges_shouldReturnPageSizeNumberOfResults_whenFirstPage() {
    FindBadgeParams params = FindBadgeParams.builder().name("%a%").build();
    List<BadgeEntity> badges = badgeManagementRepository.findBadges(params, 1, 12);
    assertThat(badges).hasSize(12);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void findBadges_shouldReturnPageSizeNumberOfResults_whenNonFirstPage() {
    FindBadgeParams params = FindBadgeParams.builder().name("%a%").build();
    List<BadgeEntity> badges = badgeManagementRepository.findBadges(params, 2, 5);
    assertThat(badges).hasSize(5);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void findBadgesForPrintBatch_shouldSearchByBatchTypeStandard() {
    BadgeEntity expectedBadgeEntity =
        BadgeEntity.builder()
            .badgeNo("KKKKKA")
            .badgeStatus(Status.ORDERED)
            .partyCode("PERSON")
            .localAuthorityShortCode("ABERD")
            .localAuthorityRef("to update")
            .appDate(LocalDate.of(2018, 6, 1))
            .appChannelCode("ONLINE")
            .startDate(LocalDate.of(2025, 5, 1))
            .expiryDate(LocalDate.of(2028, 5, 1))
            .eligibilityCode(EligibilityType.PIP)
            .imageLink("")
            .imageLinkOriginal(null)
            .deliverToCode("HOME")
            .deliverOptionCode("STAND")
            .holderName("Reginald Pai")
            .nino("")
            .dob(LocalDate.of(1953, 9, 12))
            .genderCode("MALE")
            .contactName("contact name")
            .contactBuildingStreet("building and street")
            .contactLine2("")
            .contactTownCity("Town or city")
            .contactPostcode("S637EU")
            .primaryPhoneNo("020 7014 0800")
            .secondaryPhoneNo(null)
            .contactEmailAddress("test101@mailinator.com")
            .cancelReasonCode(null)
            .replaceReasonCode(null)
            .orderDate(null)
            .numberOfBadges(0)
            .build();
    FindBadgesForPrintBatchParams params =
        FindBadgesForPrintBatchParams.builder().batchId(-1).build();
    List<BadgeEntity> badges = badgeManagementRepository.findBadgesForPrintBatch(params);
    assertThat(badges).hasSize(1);
    assertThat(badges.get(0)).isEqualTo(expectedBadgeEntity);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void updatesBadgesForPrintBatch_shouldUpdateORDEREDBadges() {
    FindBadgesForPrintBatchParams findParams =
        FindBadgesForPrintBatchParams.builder().batchId(-1).build();
    List<BadgeEntity> originalBadges =
        badgeManagementRepository.findBadgesForPrintBatch(findParams);
    assertThat(originalBadges).hasSize(1);
    assertThat(originalBadges.get(0).getBadgeStatus()).isEqualTo(Status.ORDERED);

    UpdateBadgesStatusesForBatchParams updateParams =
        UpdateBadgesStatusesForBatchParams.builder().batchId(-1).status("PROCESSED").build();
    badgeManagementRepository.updateBadgesStatusesForBatch(updateParams);

    RetrieveBadgeParams retrieveParams =
        RetrieveBadgeParams.builder().badgeNo(originalBadges.get(0).getBadgeNo()).build();
    BadgeEntity updatedBadgeEntity = badgeManagementRepository.retrieveBadge(retrieveParams);
    assertThat(updatedBadgeEntity.getBadgeStatus()).isEqualTo(Status.PROCESSED);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void deleteBadge_shouldLogicallyDeleteBadge() {
    DeleteBadgeParams deleteBadgeParams =
        DeleteBadgeParams.builder()
            .deleteStatus(BadgeEntity.Status.DELETED)
            .badgeNo("KKKKKK")
            .build();

    badgeManagementRepository.deleteBadge(deleteBadgeParams);

    RetrieveBadgeParams retrieveParams = RetrieveBadgeParams.builder().badgeNo("KKKKKK").build();
    BadgeEntity badgeEntity = badgeManagementRepository.retrieveBadge(retrieveParams);
    assertThat(badgeEntity).isNotNull();

    assertThat(badgeEntity.getBadgeStatus()).isEqualTo(BadgeEntity.Status.DELETED);
    assertThat(badgeEntity.getDeliverToCode()).isEqualTo("DELETED");
    assertThat(badgeEntity.getDeliverOptionCode()).isEqualTo("DELETED");
    assertThat(badgeEntity.getHolderName()).isEqualTo("DELETED");
    assertThat(badgeEntity.getContactBuildingStreet()).isEqualTo("DELETED");
    assertThat(badgeEntity.getContactTownCity()).isEqualTo("DELETED");
    assertThat(badgeEntity.getContactPostcode()).isEqualTo("DELETED");
    assertThat(badgeEntity.getPrimaryPhoneNo()).isEqualTo("DELETED");

    assertThat(badgeEntity.getImageLink()).isNull();
    assertThat(badgeEntity.getNino()).isNull();
    assertThat(badgeEntity.getDob()).isNull();
    assertThat(badgeEntity.getGenderCode()).isNull();
    assertThat(badgeEntity.getContactName()).isNull();
    assertThat(badgeEntity.getContactLine2()).isNull();
    assertThat(badgeEntity.getSecondaryPhoneNo()).isNull();
    assertThat(badgeEntity.getContactEmailAddress()).isNull();
    assertThat(badgeEntity.getImageLinkOriginal()).isNull();
    assertThat(badgeEntity.getNotForReassessment()).isNull();
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void replaceBadge_shouldUpdateRecord() {
    ReplaceBadgeParams params =
        ReplaceBadgeParams.builder()
            .badgeNumber("KKKKKK")
            .deliveryCode(DeliverTo.HOME)
            .deliveryOptionCode(DeliverOption.FAST)
            .reasonCode(ReplaceReason.DAMAGED)
            .startDate(LocalDate.now())
            .status(Status.REPLACED)
            .build();

    badgeManagementRepository.replaceBadge(params);

    RetrieveBadgeParams retrieveParams = RetrieveBadgeParams.builder().badgeNo("KKKKKK").build();
    BadgeEntity badgeEntity = badgeManagementRepository.retrieveBadge(retrieveParams);
    assertThat(badgeEntity).isNotNull();

    assertThat(badgeEntity.getBadgeStatus()).isEqualTo(BadgeEntity.Status.REPLACED);
    assertThat(badgeEntity.getReplaceReasonCode()).isEqualTo("DAMAGED");
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void updateStatusToIssued() {
    // Can set Issued
    assertThat(
            badgeManagementRepository.updateBadgeStatusFromStatus(
                UpdateBadgeStatusParams.builder()
                    .badgeNumber("KKKKDA")
                    .toStatus(ISSUED)
                    .fromStatus(Status.PROCESSED)
                    .build()))
        .isEqualTo(1);
    assertThat(
            badgeManagementRepository
                .retrieveBadge(RetrieveBadgeParams.builder().badgeNo("KKKKDA").build())
                .getBadgeStatus())
        .isEqualTo(ISSUED);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void updateStatusToReject() {
    // Can set Rejected
    assertThat(
            badgeManagementRepository.updateBadgeStatusFromStatus(
                UpdateBadgeStatusParams.builder()
                    .badgeNumber("KKKKDB")
                    .toStatus(REJECT)
                    .fromStatus(Status.PROCESSED)
                    .build()))
        .isEqualTo(1);
    assertThat(
            badgeManagementRepository
                .retrieveBadge(RetrieveBadgeParams.builder().badgeNo("KKKKDB").build())
                .getBadgeStatus())
        .isEqualTo(REJECT);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void updateStatusWhenDeletedDoesNowt() {
    // Can set Issued
    assertThat(
            badgeManagementRepository.updateBadgeStatusFromStatus(
                UpdateBadgeStatusParams.builder()
                    .badgeNumber("KKKKDC")
                    .toStatus(REJECT)
                    .fromStatus(Status.PROCESSED)
                    .build()))
        .isEqualTo(0);
    assertThat(
            badgeManagementRepository
                .retrieveBadge(RetrieveBadgeParams.builder().badgeNo("KKKKDC").build())
                .getBadgeStatus())
        .isEqualTo(Status.DELETED);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void retrieveBadgesByLa() {
    List<BadgeZipEntity> results = badgeManagementRepository.retrieveBadgesByLa("FINDBYLA");
    assertThat(results).extracting("badgeNo").contains("FINDA1", "FINDA3");
    for (BadgeZipEntity entity : results) {
      if (entity.getBadgeNo().equals("FINDA1")) {
        assertThat(entity.getBadgeStatus()).isEqualTo(Status.DELETED);
        assertThat(entity.getNotForReassessment()).isEqualTo(false);
      } else if (entity.getBadgeNo().equals("FINDA3")) {
        assertThat(entity.getBadgeStatus()).isEqualTo(Status.ORDERED);
        assertThat(entity.getNotForReassessment()).isEqualTo(true);
      } else {
        assertThat(entity.getBadgeStatus()).isEqualTo(Status.ISSUED);
        // Should have issued/rejected/printed data for the other badge.
        assertThat(entity.getIssuedDateTime()).isNotEmpty();
        assertThat(entity.getRejectedDateTime()).isEqualTo("2010-02-03 15:16:17");
        assertThat(entity.getRejectedReason()).isEqualTo("rejected reason");
        assertThat(entity.getPrintRequestDateTime()).isEqualTo("2011-01-01 03:00:00");
        assertThat(entity.getNotForReassessment()).isEqualTo(null);
      }
    }
  }
}
