package uk.gov.dft.bluebadge.service.badgemanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;

@Service
@Transactional
public class BadgeManagementService {

  private final BadgeManagementRepository repository;

  @Autowired
  BadgeManagementService(BadgeManagementRepository repository) {
    this.repository = repository;
  }
}
