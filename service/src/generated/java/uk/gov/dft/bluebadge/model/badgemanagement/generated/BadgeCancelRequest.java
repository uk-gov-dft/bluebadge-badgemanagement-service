package uk.gov.dft.bluebadge.model.badgemanagement.generated;

import javax.validation.constraints.NotNull;
import lombok.Data;
import uk.gov.dft.bluebadge.service.badgemanagement.model.CancelReason;

@Data
public class BadgeCancelRequest {
  @NotNull private String badgeNumber;
  @NotNull private CancelReason cancelReasonCode;
}
