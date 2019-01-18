package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.PrintServiceApiClient;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.PrintBatchBadgeRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.PrintBatchRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.ProcessedBadge;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.ProcessedBatch;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.ProcessedBatchesResponse;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.BadgeConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.model.BatchType;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BatchRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgesForPrintBatchParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.LinkBadgeToBatchParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.UpdateBadgeStatusParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.UpdateBadgesStatusesForBatchParams;

@Slf4j
@Service
@SuppressWarnings("squid:S1612")
@Transactional(propagation = Propagation.REQUIRED)
public class BatchService {
  private static final DateTimeFormatter dateTimeFormatter =
      new DateTimeFormatterBuilder()
          .appendValue(YEAR, 4)
          .appendValue(MONTH_OF_YEAR, 2)
          .appendValue(DAY_OF_MONTH, 2)
          .appendValue(HOUR_OF_DAY, 2)
          .appendValue(MINUTE_OF_HOUR, 2)
          .appendValue(SECOND_OF_MINUTE, 2)
          .toFormatter();

  private BadgeManagementRepository badgeRepository;
  private BatchRepository batchRepository;
  private PrintServiceApiClient printServiceApiClient;

  @Autowired
  BatchService(
      BadgeManagementRepository badgeRepository,
      BatchRepository batchRepository,
      PrintServiceApiClient printServiceApiClient) {
    this.badgeRepository = badgeRepository;
    this.batchRepository = batchRepository;
    this.printServiceApiClient = printServiceApiClient;
  }

  public void sendPrintBatch(BatchType batchType) {
    BatchEntity batchEntity =
        batchRepository.createBatch(
            BatchEntity.SourceEnum.DFT,
            BatchEntity.PurposeEnum.fromBatchType(batchType),
            getFilename(LocalDateTime.now()));
    batchRepository.appendBadgesToBatch(batchEntity.getId(), batchType);

    FindBadgesForPrintBatchParams findBadgeParams =
        FindBadgesForPrintBatchParams.builder().batchId(batchEntity.getId()).build();
    List<BadgeEntity> badgesForPrintBatch =
        badgeRepository.findBadgesForPrintBatch(findBadgeParams);

    if (!badgesForPrintBatch.isEmpty()) {
      PrintBatchRequest batch = toBatch(batchType, batchEntity, badgesForPrintBatch);
      printServiceApiClient.printBatch(batch);

      UpdateBadgesStatusesForBatchParams paramsUpdate =
          UpdateBadgesStatusesForBatchParams.builder()
              .batchId(batchEntity.getId())
              .status("PROCESSED")
              .build();
      badgeRepository.updateBadgesStatusesForBatch(paramsUpdate);
    } else {
      log.info(
          "Print batch request not sent because there are no badges associated to batch id [{}].",
          batchEntity.getId());
    }
  }

  private PrintBatchRequest toBatch(
      BatchType batchType, BatchEntity batchEntity, List<BadgeEntity> badges) {
    List<PrintBatchBadgeRequest> badgesForPrintRequest =
        (badges.stream().map(this::toBadgePrintRequest)).collect(Collectors.toList());
    return PrintBatchRequest.builder()
        .batchType(batchType.name())
        .filename(batchEntity.getFilename())
        .badges(badgesForPrintRequest)
        .build();
  }

  private PrintBatchBadgeRequest toBadgePrintRequest(BadgeEntity badgeEntity) {
    BadgeConverter converter = new BadgeConverter();
    uk.gov.dft.bluebadge.model.badgemanagement.generated.Badge badge =
        converter.convertToModel(badgeEntity);
    return PrintBatchBadgeRequest.builder()
        .badgeNumber(badgeEntity.getBadgeNo())
        .party(badge.getParty())
        .deliverToCode(badgeEntity.getDeliverToCode())
        .deliveryOptionCode(badgeEntity.getDeliverOptionCode())
        .localAuthorityShortCode(badgeEntity.getLocalAuthorityShortCode())
        .startDate(badgeEntity.getStartDate())
        .expiryDate(badgeEntity.getExpiryDate())
        .deliverToCode(badgeEntity.getDeliverToCode())
        .imageLink(badgeEntity.getImageLink())
        .build();
  }

  public ProcessedBatchesResponse collectBatches() {
    return printServiceApiClient.collectPrintBatchResults();
  }

  public void processBatch(ProcessedBatch batch) {

    if (StringUtils.isNotEmpty(batch.getErrorMessage())) {
      // TODO: This should perhaps be written to batch table in a new column
      log.error(
          "Could not process print batch result for {} due to error from print service: {}",
          batch.getFilename(),
          batch.getErrorMessage());
      return;
    }

    BatchEntity batchEntity =
        batchRepository.createBatch(
            BatchEntity.SourceEnum.PRINTER,
            batchIsConfirmation(batch)
                ? BatchEntity.PurposeEnum.ISSUED
                : BatchEntity.PurposeEnum.REJECTED,
            batch.getFilename());

    // Update each badge in the batch results file and link to new batch
    for (ProcessedBadge badge : batch.getProcessedBadges()) {

      batchRepository.linkBadgeToBatch(
          LinkBadgeToBatchParams.builder()
              .badgeId(badge.getBadgeNumber())
              .batchId(batchEntity.getId())
              .build());
      int result =
          badgeRepository.updateBadgeStatusFromStatus(
              UpdateBadgeStatusParams.builder()
                  .badgeNumber(badge.getBadgeNumber())
                  .toStatus(
                      batchIsConfirmation(batch)
                          ? BadgeEntity.Status.ISSUED
                          : BadgeEntity.Status.REJECT)
                  .fromStatus(BadgeEntity.Status.PROCESSED)
                  .build());
      if (0 == result) {
        log.warn(
            "Processing print batch {}, badge {} resulted in no badge status change. Perhaps was not expected status of PROCESSED.",
            batch.getFilename(),
            badge.getBadgeNumber());
      }
    }
    printServiceApiClient.deleteBatchConfirmation(batch.getFilename());
    log.info("Finished processing batch {}", batch.getFilename());
  }

  private boolean batchIsConfirmation(ProcessedBatch batch) {
    return batch.getFileType() == ProcessedBatch.FileTypeEnum.CONFIRMATION;
  }

  private String getFilename(LocalDateTime localDateTime) {
    StringBuilder filename = new StringBuilder().append("BADGEEXTRACT_");
    String localDateTimeString = localDateTime.format(dateTimeFormatter);
    return filename.append(localDateTimeString).toString();
  }
}
