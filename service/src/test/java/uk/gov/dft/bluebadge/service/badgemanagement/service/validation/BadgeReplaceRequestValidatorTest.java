package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import org.junit.Test;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeReplaceRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.model.DeliverOption;
import uk.gov.dft.bluebadge.service.badgemanagement.model.DeliverTo;

public class BadgeReplaceRequestValidatorTest {
  @Test
  public void validate_ok() {
    BadgeReplaceRequest request = new BadgeReplaceRequest();
    request.setDeliverToCode(DeliverTo.HOME);
    request.setDeliveryOptionCode(DeliverOption.FAST);
    BadgeReplaceRequestValidator.validate(request);
    request.setDeliveryOptionCode(DeliverOption.STAND);
    BadgeReplaceRequestValidator.validate(request);
    request.setDeliverToCode(DeliverTo.COUNCIL);
    BadgeReplaceRequestValidator.validate(request);
  }

  @Test(expected = BadRequestException.class)
  public void validate_invalid() {
    BadgeReplaceRequest request = new BadgeReplaceRequest();
    request.setDeliverToCode(DeliverTo.COUNCIL);
    request.setDeliveryOptionCode(DeliverOption.FAST);
    BadgeReplaceRequestValidator.validate(request);
  }
}
