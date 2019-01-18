package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import java.time.Instant;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.ProcessedBadge;

@Data
@Builder
@EqualsAndHashCode
public class BatchBadgeLinkEntity {
  private int batchId;
  @NotNull private String badgeId;
  private String localAuthorityShortCode;
  private Instant issuedDateTime;
  private String rejectedReason;
  private ProcessedBadge.CancellationEnum cancellation;
}
