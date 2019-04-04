package uk.gov.dft.bluebadge.service.badgemanagement.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class BadgeNumberService {

  private final BadgeManagementRepository repository;

  public BadgeNumberService(BadgeManagementRepository repository) {
    this.repository = repository;
  }

  public Integer getBagdeNumber() {
    return repository.retrieveNextBadgeNumber();
  }
}
