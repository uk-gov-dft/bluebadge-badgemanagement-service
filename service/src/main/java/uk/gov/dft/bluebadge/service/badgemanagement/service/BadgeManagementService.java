package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static uk.gov.dft.bluebadge.service.badgemanagement.service.ValidationKeyEnum.MISSING_FIND_PARAMS;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.ValidationKeyEnum.TOO_MANY_FIND_PARAMS;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.service.exception.BadRequestException;

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
}
