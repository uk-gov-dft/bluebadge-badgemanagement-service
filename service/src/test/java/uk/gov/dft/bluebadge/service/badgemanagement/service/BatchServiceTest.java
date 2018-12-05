package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static org.mockito.Mockito.*;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestBase;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.*;
import uk.gov.dft.bluebadge.service.badgemanagement.service.audit.BadgeAuditLogger;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.BlacklistedCombinationsFilter;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidateBadgeOrder;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidateCancelBadge;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidateReplaceBadge;

public class BatchServiceTest extends BadgeTestBase {
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

  private BatchService service;

  @Before
  public void setUp() {
    when(securityUtilsMock.getCurrentLocalAuthorityShortCode())
        .thenReturn(LOCAL_AUTHORITY_SHORT_CODE);

    service = new BatchService(repositoryMock);
  }
  /*
    @Test
    public void findBadgesForPrintBatch_ok() {
      BadgeEntity badgeEntity1 = BadgeEntity.builder().build();
      BadgeEntity badgeEntity2 = BadgeEntity.builder().build();
      List<BadgeEntity> expectedBadges = Lists.newArrayList(badgeEntity1, badgeEntity2);
      FindBadgesForPrintBatchParams params =
          FindBadgesForPrintBatchParams.builder().batchType("STANDARD").build();
      when(repositoryMock.findBadgesForPrintBatch(params))
          .thenReturn(Lists.newArrayList(expectedBadges));

      List<BadgeEntity> badges = service.findBadgesForPrintBatch("STANDARD");
      assertThat(badges).isEqualTo(expectedBadges);
    }
  */

  @Test
  public void sendPrintBatchSTANDARDBatchType_shouldWork() {
    service.sendPrintBatch("STANDARD");
  }

  @Test
  public void sendPrintBatchFASTTRACKBatchType_shouldWork() {
    service.sendPrintBatch("FASTTRACK");
  }

  @Test
  public void sendPrintBatchLABatchType_shouldWork() {
    service.sendPrintBatch("LA");
  }
}
