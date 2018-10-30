package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableSet;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.badgemanagement.ApplicationContextTests;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.DeleteBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.ReplaceBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.RetrieveBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status;

@RunWith(SpringRunner.class)
@SqlGroup({@Sql(scripts = "classpath:/test-data.sql")})
@Transactional
public class BadgeManagementRepositoryIntTest extends ApplicationContextTests {

  @Autowired BadgeManagementRepository badgeManagementRepository;

  @Test
  public void retrieveBadge_ok() {
    RetrieveBadgeParams retrieveParams = RetrieveBadgeParams.builder().badgeNo("KKKKKK").build();
    BadgeEntity badgeEntity = badgeManagementRepository.retrieveBadge(retrieveParams);
    assertThat(badgeEntity).isNotNull();

    assertThat(badgeEntity.getBadgeNo()).isEqualTo("KKKKKK");
    assertThat(badgeEntity.getBadgeStatus()).isEqualTo(BadgeEntity.Status.ISSUED);

    assertThat(badgeEntity.getImageLink()).isEqualTo("badge/KKKKKK/thumbnail.jpg");
    assertThat(badgeEntity.getImageLinkOriginal()).isEqualTo("badge/KKKKKK/original.jpg");
  }

  @Test
  public void cancelBadge_ok() {
    CancelBadgeParams params =
        CancelBadgeParams.builder()
            .badgeNo("KKKKKK")
            .cancelReasonCode("reason")
            .localAuthorityShortCode("ABERD")
            .build();
    int recordsAffected = badgeManagementRepository.cancelBadge(params);
    assertThat(recordsAffected).isEqualTo(1);
  }

  @Test
  public void cancelBadge_badgeNotExist() {
    CancelBadgeParams params =
        CancelBadgeParams.builder()
            .badgeNo("NOTEXI")
            .cancelReasonCode("reason")
            .localAuthorityShortCode("ABERD")
            .build();
    int recordsAffected = badgeManagementRepository.cancelBadge(params);
    assertThat(recordsAffected).isEqualTo(0);
  }

  @Test
  public void cancelBadge_existsInDifferentLocalAuthority() {
    CancelBadgeParams params =
        CancelBadgeParams.builder()
            .badgeNo("KKKKKK")
            .cancelReasonCode("reason")
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
              .badgeStatus(BadgeEntity.Status.ISSUED)
              .contactName("Jane" + id)
              .partyCode("PAR")
              .localAuthorityShortCode("WINMD")
              .appDate(LocalDate.now())
              .appChannelCode("APC")
              .startDate(INITIAL_START_DATE.plusDays(id))
              .expiryDate(INITIAL_START_DATE.plusDays(id).plusYears(2))
              .deliverToCode("DE")
              .deliverOptionCode("DOPT")
              .holderName("Jane" + id)
              .contactBuildingStreet("building" + id)
              .contactTownCity("town" + id)
              .contactPostcode("CPC111")
              .primaryPhoneNo("01478523698")
              .orderDate(LocalDate.now())
              .build();
      badgeManagementRepository.createBadge(badgeEntity);
      badgeEntityList.add(badgeEntity);
    }

    FindBadgeParams params = FindBadgeParams.builder().name("%JANE%").build();
    List<BadgeEntity> badges = badgeManagementRepository.findBadges(params);

    Collections.sort(badgeEntityList, (b1, b2) -> b2.getStartDate().compareTo(b1.getStartDate()));
    List<BadgeEntity> expectedBadgeEntityList =
        badgeEntityList
            .stream()
            .limit(RESULTS_LIMIT)
            .map(
                b -> {
                  b.setOrderDate(null);
                  return b;
                })
            .collect(Collectors.toList());

    assertThat(badges).hasSize(RESULTS_LIMIT);
    assertThat(badges).isEqualTo(expectedBadgeEntityList);
  }

  @Test
  public void findBadges_shouldSearchByStatus() {
    Set<String> statuses = ImmutableSet.of(BadgeEntity.Status.ISSUED.name());
    FindBadgeParams params = FindBadgeParams.builder().statuses(statuses).build();
    List<BadgeEntity> badges = badgeManagementRepository.findBadges(params);
    assertThat(badges).isNotEmpty();
    assertThat(badges).extracting("badgeStatus").containsOnly(BadgeEntity.Status.ISSUED);
  }

  @Test
  public void findBadges_shouldSearchByStatus_deleted() {
    Set<String> statuses = ImmutableSet.of(BadgeEntity.Status.DELETED.name());
    FindBadgeParams params = FindBadgeParams.builder().statuses(statuses).build();
    List<BadgeEntity> badges = badgeManagementRepository.findBadges(params);
    assertThat(badges).isNotEmpty();
    assertThat(badges).extracting("badgeStatus").containsOnly(BadgeEntity.Status.DELETED);
  }

  @Test
  public void findBadges_shouldSearchByStatusAndPostCode() {
    Set<String> statuses = ImmutableSet.of(BadgeEntity.Status.ISSUED.name());
    FindBadgeParams params =
        FindBadgeParams.builder().postcode("S637FU").statuses(statuses).build();
    List<BadgeEntity> badges = badgeManagementRepository.findBadges(params);
    assertThat(badges).isNotEmpty();
    assertThat(badges).extracting("badgeStatus").containsOnly(BadgeEntity.Status.ISSUED);
    assertThat(badges).extracting("contactPostcode").containsOnly("S637FU");
  }

  @Test
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
  }

  @Test
  public void replaceBadge_shouldUpdateRecord() {
    ReplaceBadgeParams params = ReplaceBadgeParams.builder()
				.badgeNumber("KKKKKK")
	    		.deliveryCode("HOME")
	    		.deliveryOptionCode("FAST")
	    		.reasonCode("DAMAGED")
	    		.startDate(LocalDate.now())
	    		.status(Status.REPLACED)
	    		.build();

    badgeManagementRepository.replaceBadge(params);

    RetrieveBadgeParams retrieveParams = RetrieveBadgeParams.builder().badgeNo("KKKKKK").build();
    BadgeEntity badgeEntity = badgeManagementRepository.retrieveBadge(retrieveParams);
    assertThat(badgeEntity).isNotNull();

    assertThat(badgeEntity.getBadgeStatus()).isEqualTo(BadgeEntity.Status.REPLACED);
    assertThat(badgeEntity.getDeliverToCode()).isEqualTo("HOME");
    assertThat(badgeEntity.getDeliverOptionCode()).isEqualTo("FAST");
    assertThat(badgeEntity.getReplaceReasonCode()).isEqualTo("DAMAGED");
    assertThat(badgeEntity.getStartDate()).isEqualTo(LocalDate.now());

   }
}
