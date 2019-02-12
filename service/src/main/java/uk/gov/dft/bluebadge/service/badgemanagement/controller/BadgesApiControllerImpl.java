package uk.gov.dft.bluebadge.service.badgemanagement.controller;

import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.INVALID_BADGE_NUMBER;

import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dft.bluebadge.common.controller.AbstractController;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeCancelRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeNumberResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeNumbersResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeReplaceRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgesResponse;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.ProcessedBatch;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.ProcessedBatchesResponse;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.BadgeConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.BadgeSummaryConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.CancelBadgeRequestConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.ReplaceBadgeRequestConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.generated.controller.BadgesApi;
import uk.gov.dft.bluebadge.service.badgemanagement.model.PrintBatchRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.BadgeManagementService;
import uk.gov.dft.bluebadge.service.badgemanagement.service.BatchService;

@RestController
public class BadgesApiControllerImpl extends AbstractController implements BadgesApi {

  private final BadgeManagementService badgeService;
  private final BatchService batchService;

  private final BadgeSummaryConverter badgeSummaryConverter;

  @SuppressWarnings("unused")
  @Autowired
  public BadgesApiControllerImpl(
      BadgeManagementService service,
      BatchService batchService,
      BadgeSummaryConverter badgeSummaryConverter) {
    this.badgeService = service;
    this.batchService = batchService;
    this.badgeSummaryConverter = badgeSummaryConverter;
  }

  @Override
  @PreAuthorize(
      "hasAuthority('PERM_ORDER_BADGE') and @securityUtils.isAuthorisedLACode(#badgeOrder.localAuthorityShortCode)")
  public ResponseEntity<BadgeNumbersResponse> orderBlueBadges(
      @ApiParam() @Valid @RequestBody BadgeOrderRequest badgeOrder) {

    List<String> createdList = badgeService.createBadge(badgeOrder);
    return ResponseEntity.ok(new BadgeNumbersResponse().data(createdList));
  }

  @Override
  @PreAuthorize("hasAuthority('PERM_FIND_BADGES')")
  public ResponseEntity<BadgesResponse> findBlueBadge(
      @Size(max = 100)
          @ApiParam(value = "Search the badge holder's name.")
          @Valid
          @RequestParam(value = "name", required = false)
          Optional<String> name,
      @Size(max = 20)
          @ApiParam(value = "A valid postcode with or without spaces.")
          @Valid
          @RequestParam(value = "postCode", required = false)
          Optional<String> postCode) {

    List<BadgeEntity> badgeEntities =
        badgeService.findBadges(name.orElse(null), postCode.orElse(null));
    return ResponseEntity.ok(
        new BadgesResponse().data(badgeSummaryConverter.convertToModelList(badgeEntities)));
  }

  @Override
  @PreAuthorize("hasAuthority('PERM_VIEW_BADGE_DETAILS')")
  public ResponseEntity<BadgeResponse> retrieveBlueBadge(
      @ApiParam(value = "A valid badge number.", required = true) @PathVariable("badgeNumber")
          String badgeNumber) {
    BadgeConverter converter = new BadgeConverter();
    BadgeEntity entity = badgeService.retrieveBadge(badgeNumber);
    return ResponseEntity.ok(new BadgeResponse().data(converter.convertToModel(entity)));
  }

  @Override
  @PreAuthorize("hasAuthority('PERM_CANCEL_BADGE') and @badgeSecurity.isAuthorised(#badgeNumber)")
  public ResponseEntity<Void> cancelBlueBadge(
      @ApiParam(value = "A valid badge number.", required = true) @PathVariable("badgeNumber")
          String badgeNumber,
      @ApiParam() @Valid @RequestBody BadgeCancelRequest badgeCancel) {
    if (!badgeNumber.equalsIgnoreCase(badgeCancel.getBadgeNumber())) {
      throw new BadRequestException(INVALID_BADGE_NUMBER.getFieldErrorInstance());
    }
    CancelBadgeRequestConverter converter = new CancelBadgeRequestConverter();
    badgeService.cancelBadge(converter.convertToEntity(badgeCancel));
    return ResponseEntity.ok().build();
  }

  @Override
  @PreAuthorize("hasAuthority('PERM_DELETE_BADGE') and @badgeSecurity.isAuthorised(#badgeNumber)")
  public ResponseEntity<Void> deleteBlueBadge(@PathVariable String badgeNumber) {
    badgeService.deleteBadge(badgeNumber);
    return ResponseEntity.ok().build();
  }

  @Override
  @PreAuthorize("hasAuthority('PERM_REPLACE_BADGE') and @badgeSecurity.isAuthorised(#badgeNumber)")
  public ResponseEntity<BadgeNumberResponse> replaceBlueBadge(
      @PathVariable String badgeNumber, @Valid @RequestBody BadgeReplaceRequest request) {
    if (!badgeNumber.equalsIgnoreCase(request.getBadgeNumber())) {
      throw new BadRequestException(INVALID_BADGE_NUMBER.getSystemErrorInstance());
    }

    ReplaceBadgeRequestConverter converter = new ReplaceBadgeRequestConverter();
    String newBadgeNumber = badgeService.replaceBadge(converter.convertToEntity(request));

    return ResponseEntity.ok(new BadgeNumberResponse().data(newBadgeNumber));
  }

  @Override
  @PreAuthorize("#oauth2.hasScope('print-batch')")
  public ResponseEntity<Void> printBatch(@Valid @RequestBody PrintBatchRequest printBadgeRequest) {
    batchService.sendPrintBatch(printBadgeRequest.getBatchType());
    return ResponseEntity.ok().build();
  }

  @Override
  @PreAuthorize("#oauth2.hasScope('print-batch')")
  public ResponseEntity<Void> reprintBatch(@Valid @PathVariable("batchId") String batchId) {
    batchService.rePrintBatch(batchId);
    return ResponseEntity.ok().build();
  }

  @Override
  @PreAuthorize("#oauth2.hasScope('print-batch')")
  public ResponseEntity<Void> collectBatches() {
    ProcessedBatchesResponse batchesResponse = batchService.collectBatches();
    for (ProcessedBatch batch : batchesResponse.getData()) {
      try {
        // Call service per batch, so transaction per batch.
        batchService.processBatch(batch);
      } catch (Exception e) {
        // Catch any DB exceptions etc.. and process next file.
        log.error("Unexpected exception processing " + batch.getFilename(), e);
      }
    }
    return ResponseEntity.ok().build();
  }
}


