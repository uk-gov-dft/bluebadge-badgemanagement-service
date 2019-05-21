package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.service.badgemanagement.model.DeliverOption;
import uk.gov.dft.bluebadge.service.badgemanagement.model.DeliverTo;
import uk.gov.dft.bluebadge.service.badgemanagement.model.ReplaceReason;

@Data
@Builder
public class ReplaceBadgeParams {

  private final String badgeNumber;
  private final BadgeEntity.Status status;
  private final ReplaceReason reasonCode;
  private final DeliverTo deliveryCode;
  private final DeliverOption deliveryOptionCode;
  private final LocalDate startDate;
}
