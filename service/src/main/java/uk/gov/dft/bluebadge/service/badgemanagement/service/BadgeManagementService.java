package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status.DELETED;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.MISSING_FIND_PARAMS;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.TOO_MANY_FIND_PARAMS;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.common.util.Base20;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.BadgeOrderRequestConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.DeleteBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.RetrieveBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidateBadgeOrder;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidateCancelBadge;

@Slf4j
@Service
@Transactional
public class BadgeManagementService {
  private static final Set<String> DEFAULT_SEARCH_STATUSES =
      EnumSet.complementOf(EnumSet.of(DELETED))
          .stream()
          .map(BadgeEntity.Status::name)
          .collect(Collectors.toSet());

  private final BadgeManagementRepository repository;
  private final ValidateBadgeOrder validateBadgeOrder;
  private final ValidateCancelBadge validateCancelBadge;
  private final SecurityUtils securityUtils;
  private final PhotoService photoService;

  @Autowired
  BadgeManagementService(
      BadgeManagementRepository repository,
      ValidateBadgeOrder validateBadgeOrder,
      ValidateCancelBadge validateCancelBadge,
      SecurityUtils securityUtils,
      PhotoService photoService) {
    this.repository = repository;
    this.validateBadgeOrder = validateBadgeOrder;
    this.validateCancelBadge = validateCancelBadge;
    this.securityUtils = securityUtils;
    this.photoService = photoService;
  }

  public List<String> createBadge(BadgeOrderRequest model) {
    BadgeEntity entity = new BadgeOrderRequestConverter().convertToEntity(model);

    List<String> createdList = new ArrayList<>();
    log.debug("Creating {} badge orders.", entity.getNumberOfBadges());

    entity.setLocalAuthorityShortCode(securityUtils.getCurrentLocalAuthorityShortCode());

    validateBadgeOrder.validate(entity);

    for (int i = 0; i < entity.getNumberOfBadges(); i++) {
      String newBadgeNo = createNewBadgeNumber();
      entity.setBadgeNo(newBadgeNo);
      if (entity.isPerson() && !StringUtils.isEmpty(model.getImageFile())) {
        S3KeyNames names = photoService.photoUpload(model.getImageFile(), newBadgeNo);
        entity.setImageLink(names.getThumbnailKeyName());
        entity.setImageLinkOriginal(names.getOriginalKeyName());
      }
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
        FindBadgeParams.builder()
            .name(nameStripped)
            .postcode(postcodeStripped)
            .statuses(DEFAULT_SEARCH_STATUSES)
            .build();
    return repository.findBadges(params);
  }

  public BadgeEntity retrieveBadge(String badgeNumber) {
    RetrieveBadgeParams params = RetrieveBadgeParams.builder().badgeNo(badgeNumber).build();
    BadgeEntity entity = repository.retrieveBadge(params);

    if (null == entity || DELETED == entity.getBadgeStatus()) {
      throw new NotFoundException("badge", NotFoundException.Operation.RETRIEVE);
    }
    entity.setImageLink(photoService.generateSignedS3Url(entity.getImageLink()));
    entity.setImageLinkOriginal(photoService.generateSignedS3Url(entity.getImageLinkOriginal()));
    return entity;
  }

  public void cancelBadge(CancelBadgeParams request) {
    // Validate the request
    validateCancelBadge.validateRequest(request);

    // Optimistically try cancel before validating to save reading badge data.
    int updates = repository.cancelBadge(request);

    if (updates == 0) {
      // Cancel did not happen.
      // Find out why cancel was invalid
      BadgeEntity badgeEntity =
          repository.retrieveBadge(
              RetrieveBadgeParams.builder().badgeNo(request.getBadgeNo()).build());
      validateCancelBadge.validateAfterFailedCancel(badgeEntity);
    }
  }

  public void deleteBadge(String badgeNumber) {
    log.info("Deleting badge {}", badgeNumber);
    RetrieveBadgeParams params = RetrieveBadgeParams.builder().badgeNo(badgeNumber).build();
    BadgeEntity badge = repository.retrieveBadge(params);

    if (null == badge || DELETED == badge.getBadgeStatus()) {
      throw new NotFoundException("badge", NotFoundException.Operation.RETRIEVE);
    }

    if (null != badge.getImageLink()) {
      photoService.deletePhoto(badgeNumber, badge.getImageLink());
    }
    if (null != badge.getImageLinkOriginal()) {
      photoService.deletePhoto(badgeNumber, badge.getImageLinkOriginal());
    }
    DeleteBadgeParams deleteBadgeParams =
        DeleteBadgeParams.builder().badgeNo(badgeNumber).deleteStatus(DELETED).build();

    repository.deleteBadge(deleteBadgeParams);
  }
}
