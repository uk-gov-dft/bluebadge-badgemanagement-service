package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.MISSING_FIND_PARAMS;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.TOO_MANY_FIND_PARAMS;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.common.security.model.LocalAuthority;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.RetrieveBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidateBadgeOrder;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidateCancelBadge;

@Slf4j
@Service
@Transactional
public class BadgeManagementService {

  private final BadgeManagementRepository repository;
  private final ValidateBadgeOrder validateBadgeOrder;
  private final ValidateCancelBadge validateCancelBadge;
  private final SecurityUtils securityUtils;

  @Autowired
  BadgeManagementService(
      BadgeManagementRepository repository,
      ValidateBadgeOrder validateBadgeOrder,
      ValidateCancelBadge validateCancelBadge,
      SecurityUtils securityUtils) {
    this.repository = repository;
    this.validateBadgeOrder = validateBadgeOrder;
    this.validateCancelBadge = validateCancelBadge;
    this.securityUtils = securityUtils;
  }

  public List<String> createBadge(BadgeEntity entity) {
    List<String> createdList = new ArrayList<>();
    log.debug("Creating {} badge orders.", entity.getNumberOfBadges());

    LocalAuthority localAuthority = securityUtils.getCurrentLocalAuthority();
    entity.setLocalAuthorityId(localAuthority.getId());
    entity.setLocalAuthorityShortCode(localAuthority.getShortCode());

    validateBadgeOrder.validate(entity);
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

  public List<BadgeEntity> findBadges(String name, String postCode) {
    String nameStripped = StringUtils.stripToNull(name);
    String postcodeStripped = StringUtils.stripToNull(postCode);
    if (null == nameStripped && null == postcodeStripped) {
      throw new BadRequestException(MISSING_FIND_PARAMS.getSystemErrorInstance());
    }

    if (null != nameStripped && null != postcodeStripped) {
      throw new BadRequestException((TOO_MANY_FIND_PARAMS.getSystemErrorInstance()));
    }

    FindBadgeParams params =
        FindBadgeParams.builder().name(nameStripped).postcode(postcodeStripped).build();
    return repository.findBadges(params);
  }

  public BadgeEntity retrieveBadge(String badgeNumber) {
    RetrieveBadgeParams params = RetrieveBadgeParams.builder().badgeNo(badgeNumber).build();
    BadgeEntity entity = repository.retrieveBadge(params);
    if (null == entity) {
      throw new NotFoundException("badge", NotFoundException.Operation.RETRIEVE);
    }
    return entity;
  }

  public void cancelBadge(CancelBadgeParams request) {
    // Validate the request
    validateCancelBadge.validateRequest(request);
    // Optimistically try cancel before validating to save reading badge data.
    Integer localAuthorityId = securityUtils.getCurrentLocalAuthority().getId();
    request.setLocalAuthorityId(localAuthorityId);
    int updates = repository.cancelBadge(request);

    if (updates == 0) {
      // Cancel did not happen.
      // Find out why cancel was invalid
      BadgeEntity badgeEntity =
          repository.retrieveBadge(
              RetrieveBadgeParams.builder().badgeNo(request.getBadgeNo()).build());
      validateCancelBadge.validateAfterFailedCancel(badgeEntity, localAuthorityId);
    }
  }
}
