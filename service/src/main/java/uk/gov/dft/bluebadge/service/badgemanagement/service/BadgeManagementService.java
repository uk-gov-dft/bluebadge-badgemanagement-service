package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status.DELETED;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity.Status.ISSUED;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.MISSING_FIND_PARAMS;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.REPLACE_EXPIRY_DATE_IN_PAST;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.REPLACE_INVALID_BADGE_STATUS;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.TOO_MANY_FIND_PARAMS;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.api.model.PagedResult;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.common.util.Base20;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeSummary;
import uk.gov.dft.bluebadge.service.badgemanagement.controller.PagingParams;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.BadgeOrderRequestConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.BadgeSummaryConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeZipEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.DeleteBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.ReplaceBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.RetrieveBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.service.audit.BadgeAuditLogger;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.BlacklistedCombinationsFilter;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.BadgeOrderValidator;
import uk.gov.dft.bluebadge.service.badgemanagement.service.validation.BadgeCancelRequestValidator;

@SuppressWarnings("squid:S00107")
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class BadgeManagementService {
  private static final Set<String> DEFAULT_SEARCH_STATUSES =
      EnumSet.complementOf(EnumSet.of(DELETED))
          .stream()
          .map(BadgeEntity.Status::name)
          .collect(Collectors.toSet());
  private static final String BADGE = "badge";

  private final BadgeManagementRepository repository;
  private final BadgeOrderValidator validateBadgeOrder;
  private final BadgeCancelRequestValidator validateCancelBadge;
  private final SecurityUtils securityUtils;
  private final PhotoService photoService;
  private final BlacklistedCombinationsFilter blacklistFilter;
  private final BadgeAuditLogger badgeAuditLogger;
  private final BadgeNumberService badgeNumberService;
  private final BadgeSummaryConverter badgeSummaryConverter;

  @Autowired
  BadgeManagementService(
      BadgeManagementRepository repository,
      BadgeOrderValidator validateBadgeOrder,
      BadgeCancelRequestValidator validateCancelBadge,
      SecurityUtils securityUtils,
      PhotoService photoService,
      BadgeNumberService badgeNumberService,
      BlacklistedCombinationsFilter blacklistFilter,
      BadgeAuditLogger badgeAuditLogger,
      BadgeSummaryConverter badgeSummaryConverter) {
    this.repository = repository;
    this.validateBadgeOrder = validateBadgeOrder;
    this.validateCancelBadge = validateCancelBadge;
    this.securityUtils = securityUtils;
    this.photoService = photoService;
    this.badgeNumberService = badgeNumberService;
    this.blacklistFilter = blacklistFilter;
    this.badgeAuditLogger = badgeAuditLogger;
    this.badgeSummaryConverter = badgeSummaryConverter;
  }

  public List<String> createBadges(BadgeOrderRequest model) {
    BadgeEntity entity = new BadgeOrderRequestConverter().convertToEntity(model);

    List<String> createdList = new ArrayList<>();
    log.debug("Creating {} badge orders.", entity.getNumberOfBadges());

    entity.setLocalAuthorityShortCode(securityUtils.getCurrentLocalAuthorityShortCode());

    validateBadgeOrder.validate(entity);
    checkBadgeHashUnique(entity);

    for (int i = 0; i < entity.getNumberOfBadges(); i++) {
      createdList.add(createBadge(entity, model));
    }
    badgeAuditLogger.logCreateAuditMessage(model, createdList, log);
    return createdList;
  }

  private String createBadge(BadgeEntity entity, BadgeOrderRequest model) {
    String newBadgeNo = createNewBadgeNumber();
    entity.setBadgeNo(newBadgeNo);
    if (entity.isPerson() && !StringUtils.isEmpty(model.getImageFile())) {
      S3KeyNames names = photoService.photoUpload(model.getImageFile(), newBadgeNo);
      entity.setImageLink(names.getThumbnailKeyName());
      entity.setImageLinkOriginal(names.getOriginalKeyName());
    }
    repository.createBadge(entity);
    return entity.getBadgeNo();
  }

  void checkBadgeHashUnique(BadgeEntity entity) {
    byte[] badgeHash = BadgeHashService.getBadgeEntityHash(entity);
    entity.setBadgeHash(badgeHash);

    List<String> existing = repository.findBadgeHash(badgeHash);
    if (null != existing && !existing.isEmpty()) {
      StringBuilder existingIDs = new StringBuilder();
      for (String badgeNo : existing) {
        existingIDs.append(";").append(badgeNo);
      }
      log.warn("Attempt to create badge with same hash as following {}", existingIDs.toString());
      throw new BadRequestException(
          new Error()
              .reason("Cannot order badge, this badge has already been ordered.")
              .message("AlreadyExists.badge"));
    }
  }

  private String createNewBadgeNumber() {
    String badgeNo;
    do {
      badgeNo = Base20.encode(badgeNumberService.getBagdeNumber());
    } while (!blacklistFilter.isValid(badgeNo));
    log.debug("Assigning badge number : {}", badgeNo);

    return badgeNo;
  }

  public PagedResult<BadgeSummary> findBadges(
      String name, String postCode, PagingParams pagingParams) {
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

    return badgeSummaryConverter.convertToModelList(
        repository.findBadges(params, pagingParams.getPageNum(), pagingParams.getPageSize()));
  }

  public BadgeEntity retrieveBadge(String badgeNumber) {
    RetrieveBadgeParams params = RetrieveBadgeParams.builder().badgeNo(badgeNumber).build();
    BadgeEntity entity = repository.retrieveBadge(params);

    if (null == entity || DELETED == entity.getBadgeStatus()) {
      throw new NotFoundException(BADGE, NotFoundException.Operation.RETRIEVE);
    }
    entity.setImageLink(photoService.generateSignedS3Url(entity.getImageLink()));
    entity.setImageLinkOriginal(photoService.generateSignedS3Url(entity.getImageLinkOriginal()));
    return entity;
  }

  public void cancelBadge(CancelBadgeParams request) {

    // Optimistically try cancel before validating to save reading badge data.
    int updates = repository.cancelBadge(request);

    if (updates == 0) {
      // Cancel did not happen.
      // Find out why cancel was invalid
      BadgeEntity badgeEntity =
          repository.retrieveBadge(
              RetrieveBadgeParams.builder().badgeNo(request.getBadgeNo()).build());
      validateCancelBadge.validateAfterFailedCancel(badgeEntity);
    } else {
      // Local authority code required for logging.
      request.setLocalAuthorityShortCode(securityUtils.getCurrentLocalAuthorityShortCode());
      badgeAuditLogger.logCancelAuditMessage(request, log);
    }
  }

  public void deleteBadge(String badgeNumber) {
    log.info("Deleting badge {}", badgeNumber);
    RetrieveBadgeParams params = RetrieveBadgeParams.builder().badgeNo(badgeNumber).build();
    BadgeEntity badge = repository.retrieveBadge(params);

    if (null == badge || DELETED == badge.getBadgeStatus()) {
      throw new NotFoundException(BADGE, NotFoundException.Operation.RETRIEVE);
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

  public String replaceBadge(ReplaceBadgeParams replaceParams) {
    log.info("Replacing badge {}", replaceParams.getBadgeNumber());

    RetrieveBadgeParams retrieveParams =
        RetrieveBadgeParams.builder().badgeNo(replaceParams.getBadgeNumber()).build();
    BadgeEntity badge = repository.retrieveBadge(retrieveParams);

    replaceValidationChecks(badge);

    repository.replaceBadge(replaceParams);
    log.info("Replaced badge number {}", replaceParams.getBadgeNumber());

    String newBadgeNumber = createNewBadgeNumber();
    badge.setBadgeNo(newBadgeNumber);
    badge.setOrderDate(replaceParams.getStartDate());
    badge.setDeliverToCode(replaceParams.getDeliveryCode().name());
    badge.setDeliverOptionCode(replaceParams.getDeliveryOptionCode().name());
    badge.setBadgeStatus(BadgeEntity.Status.ORDERED);
    repository.createBadge(badge);
    log.info("Created replacement badge {}", newBadgeNumber);

    return newBadgeNumber;
  }

  private void replaceValidationChecks(BadgeEntity badge) {
    if (null == badge || DELETED == badge.getBadgeStatus()) {
      throw new NotFoundException(BADGE, NotFoundException.Operation.RETRIEVE);
    }

    if (badge.getExpiryDate().isBefore(LocalDate.now())) {
      throw new BadRequestException(REPLACE_EXPIRY_DATE_IN_PAST.getSystemErrorInstance());
    }

    if (ISSUED != badge.getBadgeStatus()) {
      throw new BadRequestException(REPLACE_INVALID_BADGE_STATUS.getSystemErrorInstance());
    }
  }

  public void retrieveBadgesByLa(OutputStream outputStream, String laShortCode) throws IOException {
    CsvMapper csvMapper = new CsvMapper();
    csvMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    CsvSchema csvSchema =
        CsvSchema.builder()
            .setUseHeader(true)
            .addColumnsFrom(csvMapper.schemaFor(BadgeZipEntity.class))
            .build();
    List<BadgeZipEntity> rows = repository.retrieveBadgesByLa(laShortCode);

    try (ZipOutputStream zippedOut = new ZipOutputStream(outputStream)) {
      ZipEntry e =
          new ZipEntry(
              LocalDate.now().format(DateTimeFormatter.ISO_DATE) + "_" + laShortCode + ".csv");
      zippedOut.putNextEntry(e);

      SequenceWriter csvWriter = csvMapper.writer(csvSchema).writeValues(zippedOut);
      csvWriter.write(rows);
      zippedOut.closeEntry();
      zippedOut.finish();
    }
  }
}
