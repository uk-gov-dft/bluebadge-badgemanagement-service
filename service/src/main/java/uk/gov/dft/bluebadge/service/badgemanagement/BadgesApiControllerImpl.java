package uk.gov.dft.bluebadge.service.badgemanagement;

import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeNumbersResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.CommonResponse;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.BadgeOrderRequestConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.generated.controller.BadgesApi;
import uk.gov.dft.bluebadge.service.badgemanagement.service.BadgeManagementService;
import uk.gov.dft.bluebadge.service.badgemanagement.service.exception.ServiceException;

@Controller
public class BadgesApiControllerImpl implements BadgesApi {

  private final BadgeManagementService service;
  private final BadgeOrderRequestConverter badgeOrderRequestConverter =
      new BadgeOrderRequestConverter();

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
        service.createBadge(badgeOrderRequestConverter.convertToEntity(badgeOrder));
    return ResponseEntity.ok(new BadgeNumbersResponse().data(createdList));
  }
}
