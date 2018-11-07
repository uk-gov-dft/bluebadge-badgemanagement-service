package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository.Statements.CANCEL;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository.Statements.CREATE;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository.Statements.FIND;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository.Statements.REPLACE;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository.Statements.RETRIEVE;

import java.time.LocalDate;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.ReplaceBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.RetrieveBadgeParams;

public class BadgeManagementRepositoryTest {

  @Mock SqlSession sqlSession;

  BadgeManagementRepository repository;

  public BadgeManagementRepositoryTest() {
    MockitoAnnotations.initMocks(this);
    repository = new BadgeManagementRepository(sqlSession);
  }

  @Test
  public void createBadge() {
    repository.createBadge(BadgeEntity.builder().build());
    verify(sqlSession).insert(eq(CREATE), any());
  }

  @Test
  public void findBadges() {
    FindBadgeParams params = FindBadgeParams.builder().name("%Bob%").postcode("WV164AW").build();
    repository.findBadges(params);
    verify(sqlSession).selectList(eq(FIND), eq(params));
  }

  @Test
  public void cancelBadge() {
    CancelBadgeParams params =
        CancelBadgeParams.builder().badgeNo("KKKKKK").cancelReasonCode("ABC").build();
    repository.cancelBadge(params);
    verify(sqlSession).update(eq(CANCEL), eq(params));
  }

  @Test
  public void retrieveBadge() {
    RetrieveBadgeParams params = RetrieveBadgeParams.builder().badgeNo("ABCDEF").build();
    repository.retrieveBadge(params);
    verify(sqlSession).selectOne(eq(RETRIEVE), eq(params));
  }

  @Test
  public void replaceBadge() {
    ReplaceBadgeParams params =
        ReplaceBadgeParams.builder()
            .badgeNumber("BAD789")
            .deliveryCode("HOME")
            .deliveryOptionCode("FAST")
            .reasonCode("DAMAGED")
            .startDate(LocalDate.now())
            .status(Status.REPLACED)
            .build();

    repository.replaceBadge(params);
    verify(sqlSession).update(eq(REPLACE), eq(params));
  }
}
