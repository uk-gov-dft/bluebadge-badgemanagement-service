package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeReplaceRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.model.DeliverOption;
import uk.gov.dft.bluebadge.service.badgemanagement.model.DeliverTo;

import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.INVALID_DELIVER_FAST_TO_COUNCIL;

public class BadgeReplaceRequestValidator {
  public static void validate(BadgeReplaceRequest request){

    if(DeliverOption.FAST == request.getDeliveryOptionCode() && DeliverTo.COUNCIL == request.getDeliverToCode()){
      throw new BadRequestException(INVALID_DELIVER_FAST_TO_COUNCIL.getFieldErrorInstance());
    }
  }
}
