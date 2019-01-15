package uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrintBatchRequest {
  private String filename;
  private String batchType;
  private List<PrintBatchBadgeRequest> badges;
}
