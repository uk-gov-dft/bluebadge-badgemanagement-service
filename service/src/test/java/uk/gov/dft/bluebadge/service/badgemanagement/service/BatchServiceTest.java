package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestBase;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BatchRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchEntity;

public class BatchServiceTest extends BadgeTestBase {
  private static final String LOCAL_AUTHORITY_SHORT_CODE = "ABERD";

  @Mock private BadgeManagementRepository badgeRepositoryMock;
  @Mock private BatchRepository batchRepositoryMock;

  @Mock private SecurityUtils securityUtilsMock;

  private BatchService service;

  BatchEntity batchEntityStandard;
  BatchEntity batchEntityFasttrack;
  BatchEntity batchEntityLA;

  @Before
  public void setUp() {
    when(securityUtilsMock.getCurrentLocalAuthorityShortCode())
        .thenReturn(LOCAL_AUTHORITY_SHORT_CODE);

    batchEntityStandard =
        BatchEntity.builder().source("DFT").purpose("PRINT").id(1).filename("filename").build();
    when(batchRepositoryMock.createBatch("STANDARD", "DFT", "PRINT"))
        .thenReturn(batchEntityStandard);
    batchEntityFasttrack =
        BatchEntity.builder().source("DFT").purpose("PRINT").id(1).filename("filename").build();
    when(batchRepositoryMock.createBatch("FASTTRACK", "DFT", "PRINT"))
        .thenReturn(batchEntityFasttrack);
    batchEntityLA =
        BatchEntity.builder().source("DFT").purpose("PRINT").id(1).filename("filename").build();
    when(batchRepositoryMock.createBatch("LA", "DFT", "PRINT")).thenReturn(batchEntityLA);

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
