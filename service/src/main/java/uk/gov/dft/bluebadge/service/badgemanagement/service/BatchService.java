package uk.gov.dft.bluebadge.service.badgemanagement.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BatchRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgesForPrintBatchParams;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class BatchService {

  private BadgeManagementRepository badgeRepository;
  private BatchRepository batchRepository;

  @Autowired
  BatchService(BadgeManagementRepository badgeRepository, BatchRepository batchRepository) {
    this.badgeRepository = badgeRepository;
    this.batchRepository = batchRepository;
  }

  public void sendPrintBatch(String batchType) {
    FindBadgesForPrintBatchParams params =
        FindBadgesForPrintBatchParams.builder().batchType(batchType).build();
    List<BadgeEntity> badges = badgeRepository.findBadgesForPrintBatch(params);
    BatchEntity batchEntity = batchRepository.createBatch(batchType, "DFT", "PRINT");
  }
}
