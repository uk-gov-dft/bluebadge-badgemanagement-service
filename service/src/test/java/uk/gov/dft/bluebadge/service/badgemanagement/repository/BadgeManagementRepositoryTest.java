package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository.Statements.CANCEL;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository.Statements.CREATE;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository.Statements.FIND;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository.Statements.FIND_BADGES_FOR_PRINT_BATCH;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository.Statements.REPLACE;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository.Statements.RETRIEVE;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository.Statements.UPDATE_BADGES_STATUSES_FOR_PRINT_BATCH;

import java.time.LocalDate;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.service.badgemanagement.model.CancelReason;
import uk.gov.dft.bluebadge.service.badgemanagement.model.DeliverOption;
import uk.gov.dft.bluebadge.service.badgemanagement.model.DeliverTo;
import uk.gov.dft.bluebadge.service.badgemanagement.model.ReplaceReason;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgesForPrintBatchParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.ReplaceBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.RetrieveBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.UpdateBadgesStatusesForBatchParams;

public class BadgeManagementRepositoryTest {

  @Mock SqlSession sqlSession;

  private BadgeManagementRepository repository;

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
    repository.findBadges(params, 1, 10);
    verify(sqlSession).selectList(eq(FIND), eq(params));
  }

  @Test(expected = IllegalArgumentException.class)
  public void findBadges_shouldThrowIllegalArgumentException_whenPageNumIsNull() {
    FindBadgeParams params = FindBadgeParams.builder().name("%Bob%").postcode("WV164AW").build();
    repository.findBadges(params, null, 10);
    verify(sqlSession, never()).selectList(any(), any());
  }

  @Test(expected = IllegalArgumentException.class)
  public void findBadges_shouldThrowIllegalArgumentException_whenPageSizeIsNull() {
    FindBadgeParams params = FindBadgeParams.builder().name("%Bob%").postcode("WV164AW").build();
    repository.findBadges(params, 1, null);
    verify(sqlSession, never()).selectList(any(), any());
  }

  @Test
  public void cancelBadge() {
    CancelBadgeParams params =
        CancelBadgeParams.builder().badgeNo("KKKKKK").cancelReasonCode(CancelReason.REVOKE).build();
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
            .deliveryCode(DeliverTo.HOME)
            .deliveryOptionCode(DeliverOption.FAST)
            .reasonCode(ReplaceReason.DAMAGED)
            .startDate(LocalDate.now())
            .status(Status.REPLACED)
            .build();

    repository.replaceBadge(params);
    verify(sqlSession).update(eq(REPLACE), eq(params));
  }

  @Test
  public void findBadgesForPrintBatch() {
    FindBadgesForPrintBatchParams params =
        FindBadgesForPrintBatchParams.builder().batchId(1).build();
    repository.findBadgesForPrintBatch(params);
    verify(sqlSession).selectList(eq(FIND_BADGES_FOR_PRINT_BATCH), eq(params));
  }

  @Test
  public void updateBadgesStatusesForPrintBatch() {
    UpdateBadgesStatusesForBatchParams params =
        UpdateBadgesStatusesForBatchParams.builder().batchId(1).status("PROCESSED").build();
    repository.updateBadgesStatusesForBatch(params);
    verify(sqlSession).update(eq(UPDATE_BADGES_STATUSES_FOR_PRINT_BATCH), eq(params));
  }
}
