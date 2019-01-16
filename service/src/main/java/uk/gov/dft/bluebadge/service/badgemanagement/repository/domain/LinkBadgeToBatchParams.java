package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class LinkBadgeToBatchParams {
  private int batchId;
  @NotNull private String badgeId;
}
