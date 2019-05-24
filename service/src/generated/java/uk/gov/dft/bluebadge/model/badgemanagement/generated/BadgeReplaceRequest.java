package uk.gov.dft.bluebadge.model.badgemanagement.generated;

import javax.validation.constraints.NotNull;
import lombok.Data;
import uk.gov.dft.bluebadge.service.badgemanagement.model.DeliverOption;
import uk.gov.dft.bluebadge.service.badgemanagement.model.DeliverTo;
import uk.gov.dft.bluebadge.service.badgemanagement.model.ReplaceReason;

@Data
public class BadgeReplaceRequest {

  @NotNull private String badgeNumber;

  @NotNull private ReplaceReason replaceReasonCode;

  @NotNull private DeliverTo deliverToCode;
  @NotNull private DeliverOption deliveryOptionCode;
}
