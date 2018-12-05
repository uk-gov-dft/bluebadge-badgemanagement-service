package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status.DELETED;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.*;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.*;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class BatchService {

  private static final Set<String> DEFAULT_SEARCH_STATUSES =
      EnumSet.complementOf(EnumSet.of(DELETED))
          .stream()
          .map(BadgeEntity.Status::name)
          .collect(Collectors.toSet());
  public static final String BADGE = "badge";

  private BadgeManagementRepository badgeRepository;

  @Autowired
  BatchService(BadgeManagementRepository badgeRepository) {
    this.badgeRepository = badgeRepository;
  }

  public void sendPrintBatch(String batchType) {
    FindBadgesForPrintBatchParams params =
        FindBadgesForPrintBatchParams.builder().batchType(batchType).build();
    List<BadgeEntity> badges = badgeRepository.findBadgesForPrintBatch(params);
    return;
  }
}
