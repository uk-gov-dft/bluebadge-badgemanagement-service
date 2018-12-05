package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestBase;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BatchRepository;

public class BatchServiceTest extends BadgeTestBase {
  private static final String LOCAL_AUTHORITY_SHORT_CODE = "ABERD";

  @Mock private BadgeManagementRepository badgeRepositoryMock;

  @Mock private BatchRepository batchRepositoryMock;

  @Mock private SecurityUtils securityUtilsMock;

  private BatchService service;

  @Before
  public void setUp() {
    when(securityUtilsMock.getCurrentLocalAuthorityShortCode())
        .thenReturn(LOCAL_AUTHORITY_SHORT_CODE);

    service = new BatchService(badgeRepositoryMock, batchRepositoryMock);
  }

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
