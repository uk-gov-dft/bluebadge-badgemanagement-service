package uk.gov.dft.bluebadge.service.badgemanagement.service.audit;

import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;

import java.time.LocalDate;

@Data
@Builder
public class BadgeCancelledAuditData {
  CancelBadgeParams cancelBadgeParams;

  public LocalDate getCancellationDate(){
    return LocalDate.now();
  }
}
