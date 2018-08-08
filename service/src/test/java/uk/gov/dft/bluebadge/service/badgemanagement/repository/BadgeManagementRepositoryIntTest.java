package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
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
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;

@RunWith(SpringRunner.class)
@SqlGroup({@Sql(scripts = "classpath:/test-data.sql")})
@Transactional
public class BadgeManagementRepositoryIntTest extends ApplicationContextTests {

  @Autowired BadgeManagementRepository badgeManagementRepository;

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
}
