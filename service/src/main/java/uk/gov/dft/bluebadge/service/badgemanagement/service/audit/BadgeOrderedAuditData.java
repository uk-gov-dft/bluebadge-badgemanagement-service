package uk.gov.dft.bluebadge.service.badgemanagement.service.audit;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.service.badgemanagement.model.BadgeOrderRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model.LocalAuthorityRefData;

@Data
@Builder
public class BadgeOrderedAuditData {
  private BadgeOrderRequest badgeOrderRequest;
  private LocalAuthorityRefData localAuthorityRefData;
  private List<String> createdBadgeNumbers;

  public LocalDate getIssuedDate() {
    return LocalDate.now();
  }
}
