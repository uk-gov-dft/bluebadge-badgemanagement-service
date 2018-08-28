package uk.gov.dft.bluebadge.service.badgemanagement;

import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dft.bluebadge.common.controller.AbstractController;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeCancelRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeNumbersResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgesResponse;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.BadgeConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.BadgeSummaryConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.CancelBadgeRequestConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.generated.controller.BadgesApi;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.BadgeManagementService;

@RestController
public class BadgesApiControllerImpl extends AbstractController implements BadgesApi {

  private final BadgeManagementService service;
  private final BadgeSummaryConverter badgeSummaryConverter;

  @SuppressWarnings("unused")
  @Autowired
  public BadgesApiControllerImpl(
      BadgeManagementService service, BadgeSummaryConverter badgeSummaryConverter) {
    this.service = service;
    this.badgeSummaryConverter = badgeSummaryConverter;
  }

  @Override
  public ResponseEntity<BadgeNumbersResponse> orderBlueBadges(
      @ApiParam() @Valid @RequestBody BadgeOrderRequest badgeOrder) {

    List<String> createdList = service.createBadge(badgeOrder);
    return ResponseEntity.ok(new BadgeNumbersResponse().data(createdList));
  }

  @Override
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

    List<BadgeEntity> badgeEntities = service.findBadges(name.orElse(null), postCode.orElse(null));
    return ResponseEntity.ok(
        new BadgesResponse().data(badgeSummaryConverter.convertToModelList(badgeEntities)));
  }

  @Override
  public ResponseEntity<BadgeResponse> retrieveBlueBadge(
      @Pattern(regexp = "^[0-9A-HJK]{6}$")
          @ApiParam(value = "A valid badge number.", required = true)
          @PathVariable("badgeNumber")
          String badgeNumber) {
    BadgeConverter converter = new BadgeConverter();
    BadgeEntity entity = service.retrieveBadge(badgeNumber);
    return ResponseEntity.ok(new BadgeResponse().data(converter.convertToModel(entity)));
  }

  @Override
  public ResponseEntity<Void> cancelBlueBadge(
      @Pattern(regexp = "^[0-9A-HJK]{6}$")
          @ApiParam(value = "A valid badge number.", required = true)
          @PathVariable("badgeNumber")
          String badgeNumber,
      @ApiParam() @Valid @RequestBody BadgeCancelRequest badgeCancel) {
    CancelBadgeRequestConverter converter = new CancelBadgeRequestConverter();
    service.cancelBadge(converter.convertToEntity(badgeCancel));
    return ResponseEntity.ok().build();
  }
}
