package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Contact;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Organisation;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Party;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeTestBase;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.PrintServiceApiClient;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.Badge;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.Batch;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BatchRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgesForPrintBatchParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.UpdateBadgesStatusesForBatchParams;

public class BatchServiceTest extends BadgeTestBase {
  private static final String LOCAL_AUTHORITY_SHORT_CODE = "ABERD";

  @Mock private BadgeManagementRepository badgeRepositoryMock;
  @Mock private BatchRepository batchRepositoryMock;
  @Mock private PrintServiceApiClient printServiceApiClientMock;

  @Mock private SecurityUtils securityUtilsMock;

  private BatchService service;

  static final Integer BATCH_ID = 1;
  static final String BADGE_NO = "KKKKK1";
  static final String FILENAME = "filename";
  static final BatchEntity BATCH_ENTITY =
      BatchEntity.builder().source("DFT").purpose("PRINT").id(BATCH_ID).filename(FILENAME).build();
  static final BadgeEntity BADGE_ENTITY =
      BadgeEntity.builder().badgeNo(BADGE_NO).badgeStatus(BadgeEntity.Status.ORDERED).build();
  static final List<BadgeEntity> BADGE_ENTITIES = Lists.newArrayList(BADGE_ENTITY);
  static final FindBadgesForPrintBatchParams FIND_BADGE_PARAMS =
      FindBadgesForPrintBatchParams.builder().batchId(BATCH_ID).build();
  static final Badge BADGE =
      Badge.builder()
          .badgeNumber(BADGE_NO)
          .party(new Party().contact(new Contact()).organisation(new Organisation()))
          .build();
  static final List<Badge> BADGES = Lists.newArrayList(BADGE);
  static final Batch BATCH =
      Batch.builder().badges(BADGES).batchType("STANDARD").filename(FILENAME).build();
  static final UpdateBadgesStatusesForBatchParams UPDATE_PARAMS =
      UpdateBadgesStatusesForBatchParams.builder().batchId(BATCH_ID).status("PROCESSED").build();

  @Before
  public void setUp() {
    when(securityUtilsMock.getCurrentLocalAuthorityShortCode())
        .thenReturn(LOCAL_AUTHORITY_SHORT_CODE);
    service = new BatchService(badgeRepositoryMock, batchRepositoryMock, printServiceApiClientMock);
  }

  @Test
  public void sendPrintBatch_shouldWork() {
    when(batchRepositoryMock.createBatch("STANDARD", "DFT", "PRINT")).thenReturn(BATCH_ENTITY);
    when(badgeRepositoryMock.findBadgesForPrintBatch(FIND_BADGE_PARAMS)).thenReturn(BADGE_ENTITIES);

    service.sendPrintBatch("STANDARD");

    verify(batchRepositoryMock).appendBadgesToBatch(BATCH_ID, "STANDARD");
    verify(printServiceApiClientMock).printBatch(BATCH);
    verify(badgeRepositoryMock).updateBadgesStatusesForBatch(UPDATE_PARAMS);
  }
}
