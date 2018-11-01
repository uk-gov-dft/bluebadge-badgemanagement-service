package uk.gov.dft.bluebadge.service.badgemanagement.service.audit;

import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model.LocalAuthorityRefData;

import java.time.LocalDate;
import java.util.List;

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
