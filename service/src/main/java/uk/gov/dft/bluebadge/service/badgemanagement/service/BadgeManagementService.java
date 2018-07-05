package uk.gov.dft.bluebadge.service.badgemanagement.service;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;

@Slf4j
@Service
@Transactional
public class BadgeManagementService {

  private final BadgeManagementRepository repository;

  @Autowired
  BadgeManagementService(BadgeManagementRepository repository) {
    this.repository = repository;
  }

  public List<String> createBadge(BadgeEntity entity) {
    List<String> createdList = new ArrayList<>();
    log.debug("Creating {} badge orders.", entity.getNumberOfBadges());

    ValidateBadgeOrder.validate(entity);
    for (int i = 0; i < entity.getNumberOfBadges(); i++) {
      entity.setBadgeNo(createNewBadgeNumber());
      repository.createBadge(entity);
      createdList.add(entity.getBadgeNo());
    }
    return createdList;
  }

  private String createNewBadgeNumber() {
    String badgeNo = Base20.encode(repository.retrieveNextBadgeNumber());
    log.debug("Assigning badge number : {}", badgeNo);

    return badgeNo;
  }
}
