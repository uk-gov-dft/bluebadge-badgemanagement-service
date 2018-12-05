package uk.gov.dft.bluebadge.service.badgemanagement.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgesForPrintBatchParams;

import java.util.List;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class BatchService {

    private BadgeManagementRepository badgeRepository;

    @Autowired
    BatchService(BadgeManagementRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    public void sendPrintBatch(String batchType) {
        FindBadgesForPrintBatchParams params =
                FindBadgesForPrintBatchParams.builder().batchType(batchType).build();
        List<BadgeEntity> badges = badgeRepository.findBadgesForPrintBatch(params);
    }
}
