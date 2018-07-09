package uk.gov.dft.bluebadge.service.badgemanagement;

import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeNumbersResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgesResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.CommonResponse;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.BadgeConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.BadgeOrderRequestConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.BadgeSummaryConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.generated.controller.BadgesApi;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.BadgeManagementService;
import uk.gov.dft.bluebadge.service.badgemanagement.service.exception.ServiceException;

@Controller
public class BadgesApiControllerImpl implements BadgesApi {

  private final BadgeManagementService service;

  @SuppressWarnings("unused")
  @Autowired
  public BadgesApiControllerImpl(BadgeManagementService service) {
    this.service = service;
  }

  @SuppressWarnings("unused")
  @ExceptionHandler({ServiceException.class})
  public ResponseEntity<CommonResponse> handleServiceException(ServiceException e) {
    return e.getResponse();
  }

  @Override
  public ResponseEntity<BadgeNumbersResponse> orderBlueBadges(
      @ApiParam() @Valid @RequestBody BadgeOrderRequest badgeOrder) {

    List<String> createdList =
        service.createBadge(new BadgeOrderRequestConverter().convertToEntity(badgeOrder));
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

    BadgeSummaryConverter converter = new BadgeSummaryConverter();
    List<BadgeEntity> badgeEntities = service.findBadges(name.orElse(null), postCode.orElse(null));
    return ResponseEntity.ok(
        new BadgesResponse().data(converter.convertToModelList(badgeEntities)));
  }

  @Override
  public ResponseEntity<BadgeResponse> retrieveBlueBadge(
      @Pattern(regexp = "^[0-9A-HK]{6}$")
      @ApiParam(value = "A valid badge number.", required = true)
      @PathVariable("badgeNumber")
          String badgeNumber) {
    BadgeConverter converter = new BadgeConverter();
    BadgeEntity entity = service.retrieveBadge(badgeNumber);
    return ResponseEntity.ok(
        new BadgeResponse().data(converter.convertToModel(entity)));
  }
}
