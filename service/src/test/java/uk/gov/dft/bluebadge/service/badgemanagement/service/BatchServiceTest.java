package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Contact;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Organisation;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Party;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.PrintServiceApiClient;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.PrintBatchBadgeRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.PrintBatchRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.ProcessedBadge;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.ProcessedBatch;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.ProcessedBatchesResponse;
import uk.gov.dft.bluebadge.service.badgemanagement.model.BatchType;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BatchRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchBadgeLinkEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgesForPrintBatchParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.UpdateBadgeStatusParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.UpdateBadgesStatusesForBatchParams;

public class BatchServiceTest {
  private static final String LOCAL_AUTHORITY_SHORT_CODE = "ABERD";

  @Mock private BadgeManagementRepository badgeRepositoryMock;
  @Mock private BatchRepository batchRepositoryMock;
  @Mock private PrintServiceApiClient printServiceApiClientMock;

  @Mock private SecurityUtils securityUtilsMock;

  public BatchServiceTest() {
    MockitoAnnotations.initMocks(this);
  }

  private BatchService service;

  private static final Integer BATCH_ID = 1;
  private static final String BADGE_NO = "KKKKK1";
  private static final String FILENAME = "filename";
  private static final BatchEntity BATCH_ENTITY_STANDARD =
      BatchEntity.builder()
          .source(BatchEntity.SourceEnum.DFT)
          .purpose(BatchEntity.PurposeEnum.STANDARD)
          .id(BATCH_ID)
          .filename(FILENAME)
          .build();
  private static final BatchEntity BATCH_ENTITY_FASTTRACK =
      BatchEntity.builder()
          .source(BatchEntity.SourceEnum.DFT)
          .purpose(BatchEntity.PurposeEnum.FASTTRACK)
          .id(BATCH_ID)
          .filename(FILENAME)
          .build();
  private static final BadgeEntity BADGE_ENTITY =
      BadgeEntity.builder().badgeNo(BADGE_NO).badgeStatus(BadgeEntity.Status.ORDERED).build();
  private static final List<BadgeEntity> BADGE_ENTITIES = Lists.newArrayList(BADGE_ENTITY);
  private static final FindBadgesForPrintBatchParams FIND_BADGE_PARAMS =
      FindBadgesForPrintBatchParams.builder().batchId(BATCH_ID).build();
  private static final PrintBatchBadgeRequest PRINT_BATCH_BADGE =
      PrintBatchBadgeRequest.builder()
          .badgeNumber(BADGE_NO)
          .party(new Party().contact(new Contact()).organisation(new Organisation()))
          .build();
  private static final List<PrintBatchBadgeRequest> PRINT_BATCH_BADGES =
      Lists.newArrayList(PRINT_BATCH_BADGE);
  private static final PrintBatchRequest BATCH =
      PrintBatchRequest.builder()
          .badges(PRINT_BATCH_BADGES)
          .batchType("STANDARD")
          .filename(FILENAME)
          .build();
  private static final UpdateBadgesStatusesForBatchParams UPDATE_PARAMS =
      UpdateBadgesStatusesForBatchParams.builder().batchId(BATCH_ID).status("PROCESSED").build();

  @Before
  public void setUp() {
    when(securityUtilsMock.getCurrentLocalAuthorityShortCode())
        .thenReturn(LOCAL_AUTHORITY_SHORT_CODE);
    service = new BatchService(badgeRepositoryMock, batchRepositoryMock, printServiceApiClientMock);
  }

  @Test
  public void sendPrintBatch_WhenThereAreBadges_shouldWork() {
    when(batchRepositoryMock.createBatch(
            eq(BatchEntity.SourceEnum.DFT), eq(BatchEntity.PurposeEnum.STANDARD), any()))
        .thenReturn(BATCH_ENTITY_STANDARD);
    when(badgeRepositoryMock.findBadgesForPrintBatch(FIND_BADGE_PARAMS)).thenReturn(BADGE_ENTITIES);

    service.sendPrintBatch(BatchType.STANDARD);

    verify(batchRepositoryMock).appendBadgesToBatch(BATCH_ID, BatchType.STANDARD);
    verify(printServiceApiClientMock).printBatch(BATCH);
    verify(badgeRepositoryMock).updateBadgesStatusesForBatch(UPDATE_PARAMS);
  }

  @Test
  public void reprintPrintBatch_GivenBatches() {
    when(batchRepositoryMock.retrieveBatchEntity(eq(BATCH_ID))).thenReturn(BATCH_ENTITY_STANDARD);
    when(badgeRepositoryMock.findBadgesForPrintBatch(FIND_BADGE_PARAMS)).thenReturn(BADGE_ENTITIES);

    service.rePrintBatch(BATCH_ID.toString());

    verify(batchRepositoryMock).retrieveBatchEntity(BATCH_ID);
    verify(printServiceApiClientMock).printBatch(BATCH);
    verify(batchRepositoryMock, never()).createBatch(any(), any(), any());
    verify(batchRepositoryMock, never()).appendBadgesToBatch(any(), any());
    verify(badgeRepositoryMock, never()).updateBadgesStatusesForBatch(UPDATE_PARAMS);
  }

  @Test
  public void sendPrintBatch_WhenThereAreNoBadges_shouldWork() {
    when(batchRepositoryMock.createBatch(
            eq(BatchEntity.SourceEnum.DFT), eq(BatchEntity.PurposeEnum.STANDARD), any()))
        .thenReturn(BATCH_ENTITY_STANDARD);
    when(badgeRepositoryMock.findBadgesForPrintBatch(FIND_BADGE_PARAMS))
        .thenReturn(Lists.newArrayList());

    service.sendPrintBatch(BatchType.STANDARD);

    verify(batchRepositoryMock).appendBadgesToBatch(BATCH_ID, BatchType.STANDARD);
    verify(printServiceApiClientMock, times(0)).printBatch(any(PrintBatchRequest.class));
    verify(badgeRepositoryMock)
        .updateBadgesStatusesForBatch(any(UpdateBadgesStatusesForBatchParams.class));
  }

  @Test
  public void processBatch() {
    // When processing 2 batch results, 1 confirmation and 1 rejection, each with 2 badges
    ProcessedBadge rejectedBadge1 =
        ProcessedBadge.builder().badgeNumber("REJEC1").errorMessage("No picture").build();
    ProcessedBadge rejectedBadge2 =
        ProcessedBadge.builder().badgeNumber("REJEC2").errorMessage("No picture").build();
    ProcessedBatch rejections =
        ProcessedBatch.builder()
            .filename("rejection.xml")
            .fileType(ProcessedBatch.FileTypeEnum.REJECTION)
            .processedBadges(newArrayList(rejectedBadge1, rejectedBadge2))
            .build();
    ProcessedBadge confirmBadge1 =
        ProcessedBadge.builder()
            .badgeNumber("CONFI1")
            .cancellation(ProcessedBadge.CancellationEnum.NO)
            .dispatchedDate(
                OffsetDateTime.of(LocalDateTime.of(2019, 1, 1, 14, 30, 59), ZoneOffset.UTC))
            .build();
    ProcessedBadge confirmBadge2 =
        ProcessedBadge.builder()
            .badgeNumber("CONFI2")
            .cancellation(ProcessedBadge.CancellationEnum.NO)
            .dispatchedDate(
                OffsetDateTime.of(LocalDateTime.of(2020, 1, 1, 14, 30, 59), ZoneOffset.UTC))
            .build();
    ProcessedBatch confirmations =
        ProcessedBatch.builder()
            .filename("confirmation.xml")
            .fileType(ProcessedBatch.FileTypeEnum.CONFIRMATION)
            .processedBadges(Lists.newArrayList(confirmBadge1, confirmBadge2))
            .build();
    when(printServiceApiClientMock.collectPrintBatchResults())
        .thenReturn(
            ProcessedBatchesResponse.builder()
                .data(Lists.newArrayList(rejections, confirmations))
                .build());
    when(batchRepositoryMock.createBatch(
            BatchEntity.SourceEnum.PRINTER, BatchEntity.PurposeEnum.REJECTED, "rejection.xml"))
        .thenReturn(BatchEntity.builder().id(10).build());
    when(batchRepositoryMock.createBatch(
            BatchEntity.SourceEnum.PRINTER, BatchEntity.PurposeEnum.ISSUED, "confirmation.xml"))
        .thenReturn(BatchEntity.builder().id(20).build());
    when(badgeRepositoryMock.updateBadgeStatusFromStatus(any())).thenReturn(1);
    when(batchRepositoryMock.badgeAlreadyProcessed(any())).thenReturn(false);

    // Call the service...
    ProcessedBatchesResponse response = service.collectBatches();
    for (ProcessedBatch batch : response.getData()) {
      service.processBatch(batch);
    }

    // Then 2 new batches are created
    verify(batchRepositoryMock, times(1))
        .createBatch(
            BatchEntity.SourceEnum.PRINTER, BatchEntity.PurposeEnum.REJECTED, "rejection.xml");
    verify(batchRepositoryMock, times(1))
        .createBatch(
            BatchEntity.SourceEnum.PRINTER, BatchEntity.PurposeEnum.ISSUED, "confirmation.xml");

    // And the badges are linked to the new batch.
    verify(batchRepositoryMock, times(1))
        .linkBadgeToBatch(
            BatchBadgeLinkEntity.builder()
                .batchId(10)
                .badgeId("REJEC1")
                .rejectedReason("No picture")
                .build());
    verify(batchRepositoryMock, times(1))
        .linkBadgeToBatch(
            BatchBadgeLinkEntity.builder()
                .batchId(10)
                .badgeId("REJEC2")
                .rejectedReason("No picture")
                .build());
    verify(batchRepositoryMock, times(1))
        .linkBadgeToBatch(
            BatchBadgeLinkEntity.builder()
                .batchId(20)
                .badgeId("CONFI1")
                .issuedDateTime(LocalDateTime.of(2019, 1, 1, 14, 30, 59).toInstant(ZoneOffset.UTC))
                .cancellation(ProcessedBadge.CancellationEnum.NO)
                .build());
    verify(batchRepositoryMock, times(1))
        .linkBadgeToBatch(
            BatchBadgeLinkEntity.builder()
                .batchId(20)
                .badgeId("CONFI1")
                .issuedDateTime(LocalDateTime.of(2019, 1, 1, 14, 30, 59).toInstant(ZoneOffset.UTC))
                .cancellation(ProcessedBadge.CancellationEnum.NO)
                .build());

    // And the 2 confirmed badges are set to ISSUED
    verify(badgeRepositoryMock, times(1))
        .updateBadgeStatusFromStatus(
            UpdateBadgeStatusParams.builder()
                .fromStatus(BadgeEntity.Status.PROCESSED)
                .toStatus(BadgeEntity.Status.ISSUED)
                .badgeNumber("CONFI1")
                .build());
    verify(badgeRepositoryMock, times(1))
        .updateBadgeStatusFromStatus(
            UpdateBadgeStatusParams.builder()
                .fromStatus(BadgeEntity.Status.PROCESSED)
                .toStatus(BadgeEntity.Status.ISSUED)
                .badgeNumber("CONFI2")
                .build());
    // And the 2 rejected badges are set to REJECT
    verify(badgeRepositoryMock, times(1))
        .updateBadgeStatusFromStatus(
            UpdateBadgeStatusParams.builder()
                .fromStatus(BadgeEntity.Status.PROCESSED)
                .toStatus(BadgeEntity.Status.REJECT)
                .badgeNumber("REJEC1")
                .build());
    verify(badgeRepositoryMock, times(1))
        .updateBadgeStatusFromStatus(
            UpdateBadgeStatusParams.builder()
                .fromStatus(BadgeEntity.Status.PROCESSED)
                .toStatus(BadgeEntity.Status.REJECT)
                .badgeNumber("REJEC2")
                .build());
    // And the 2 batch processing results are deleted
    verify(printServiceApiClientMock, times(1)).deleteBatchConfirmation("rejection.xml");
    verify(printServiceApiClientMock, times(1)).deleteBatchConfirmation("confirmation.xml");
  }

  @Test
  public void processDuplicateBatch() {
    ProcessedBadge rejectedBadge1 =
        ProcessedBadge.builder().badgeNumber("REJDUP").errorMessage("No picture").build();
    ProcessedBatch rejections =
        ProcessedBatch.builder()
            .filename("rejection_dupe.xml")
            .fileType(ProcessedBatch.FileTypeEnum.REJECTION)
            .processedBadges(newArrayList(rejectedBadge1))
            .build();
    ProcessedBadge confirmBadge1 =
        ProcessedBadge.builder()
            .badgeNumber("CONDUP")
            .cancellation(ProcessedBadge.CancellationEnum.NO)
            .dispatchedDate(
                OffsetDateTime.of(LocalDateTime.of(2019, 1, 1, 14, 30, 59), ZoneOffset.UTC))
            .build();
    ProcessedBatch confirmations =
        ProcessedBatch.builder()
            .filename("confirmation_dupe.xml")
            .fileType(ProcessedBatch.FileTypeEnum.CONFIRMATION)
            .processedBadges(Lists.newArrayList(confirmBadge1))
            .build();
    when(printServiceApiClientMock.collectPrintBatchResults())
        .thenReturn(
            ProcessedBatchesResponse.builder()
                .data(Lists.newArrayList(rejections, confirmations))
                .build());
    when(batchRepositoryMock.createBatch(
            BatchEntity.SourceEnum.PRINTER, BatchEntity.PurposeEnum.REJECTED, "rejection_dupe.xml"))
        .thenReturn(BatchEntity.builder().id(10).build());
    when(batchRepositoryMock.createBatch(
            BatchEntity.SourceEnum.PRINTER,
            BatchEntity.PurposeEnum.ISSUED,
            "confirmation_dupe.xml"))
        .thenReturn(BatchEntity.builder().id(20).build());
    when(badgeRepositoryMock.updateBadgeStatusFromStatus(any())).thenReturn(1);
    when(batchRepositoryMock.badgeAlreadyProcessed(any())).thenReturn(true);
    // Call the service...
    ProcessedBatchesResponse response = service.collectBatches();
    for (ProcessedBatch batch : response.getData()) {
      service.processBatch(batch);
    }

    // Then 2 new batches are created
    verify(batchRepositoryMock, times(1))
        .createBatch(
            BatchEntity.SourceEnum.PRINTER, BatchEntity.PurposeEnum.REJECTED, "rejection_dupe.xml");
    verify(batchRepositoryMock, times(1))
        .createBatch(
            BatchEntity.SourceEnum.PRINTER,
            BatchEntity.PurposeEnum.ISSUED,
            "confirmation_dupe.xml");

    // And the badges are NOT linked to the new batch.
    verify(batchRepositoryMock, never())
        .linkBadgeToBatch(
            BatchBadgeLinkEntity.builder()
                .batchId(10)
                .badgeId("REJDUP")
                .rejectedReason("No picture")
                .build());
    verify(batchRepositoryMock, never())
        .linkBadgeToBatch(
            BatchBadgeLinkEntity.builder()
                .batchId(20)
                .badgeId("CONDUP")
                .issuedDateTime(LocalDateTime.of(2019, 1, 1, 14, 30, 59).toInstant(ZoneOffset.UTC))
                .cancellation(ProcessedBadge.CancellationEnum.NO)
                .build());

    // And the 2 badges are Unchanged
    verify(badgeRepositoryMock, never())
        .updateBadgeStatusFromStatus(
            UpdateBadgeStatusParams.builder()
                .fromStatus(BadgeEntity.Status.PROCESSED)
                .toStatus(BadgeEntity.Status.ISSUED)
                .badgeNumber("CONDUP")
                .build());

    verify(badgeRepositoryMock, never())
        .updateBadgeStatusFromStatus(
            UpdateBadgeStatusParams.builder()
                .fromStatus(BadgeEntity.Status.PROCESSED)
                .toStatus(BadgeEntity.Status.REJECT)
                .badgeNumber("REJDUP")
                .build());

    // And the 2 batch processing results are deleted
    verify(printServiceApiClientMock, times(1)).deleteBatchConfirmation("rejection_dupe.xml");
    verify(printServiceApiClientMock, times(1)).deleteBatchConfirmation("confirmation_dupe.xml");
  }
}
