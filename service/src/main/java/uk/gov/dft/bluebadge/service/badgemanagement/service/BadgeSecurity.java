package uk.gov.dft.bluebadge.service.badgemanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;

@Service("badgeSecurity")
public class BadgeSecurity {
  @Autowired private final BadgeManagementService badgeManagementService;
  @Autowired private final SecurityUtils securityUtils;

  @Autowired
  public BadgeSecurity(BadgeManagementService badgeManagementService, SecurityUtils securityUtils) {
    this.badgeManagementService = badgeManagementService;
    this.securityUtils = securityUtils;
  }

  public boolean isAuthorised(String badgeNumber) {
    BadgeEntity badge = badgeManagementService.retrieveBadge(badgeNumber);
    return securityUtils.isAuthorisedLACode(badge.getLocalAuthorityShortCode());
  }
}
