package uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Party;

@Data
@Builder
public class PrintBatchBadgeRequest {
  private String badgeNumber;
  private Party party;
  private String localAuthorityShortCode;
  private LocalDate startDate;
  private LocalDate expiryDate;
  private String deliverToCode;
  private String deliveryOptionCode;
  private String imageLink;
}
