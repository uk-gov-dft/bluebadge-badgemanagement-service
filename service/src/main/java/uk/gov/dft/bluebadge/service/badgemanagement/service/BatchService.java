package uk.gov.dft.bluebadge.service.badgemanagement.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.PrintServiceApiClient;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.PrintBatchBadgeRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.PrintBatchRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.BadgeConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BatchRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgesForPrintBatchParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.UpdateBadgesStatusesForBatchParams;

@Slf4j
@Service
@SuppressWarnings("squid:S1612")
@Transactional(propagation = Propagation.REQUIRED)
public class BatchService {

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

  public void sendPrintBatch(String batchType) {
    BatchEntity batchEntity = batchRepository.createBatch(batchType, "DFT", "PRINT");
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

  private PrintBatchRequest toBatch(String batchType, BatchEntity batchEntity, List<BadgeEntity> badges) {
    List<PrintBatchBadgeRequest>
        badgesForPrintRequest =
            (badges.stream().map(b -> toBadgePrintRequest(b))).collect(Collectors.toList());
    return PrintBatchRequest.builder()
        .batchType(batchType)
        .filename(batchEntity.getFilename())
        .badges(badgesForPrintRequest)
        .build();
  }

  private PrintBatchBadgeRequest
      toBadgePrintRequest(BadgeEntity badgeEntity) {
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
}
