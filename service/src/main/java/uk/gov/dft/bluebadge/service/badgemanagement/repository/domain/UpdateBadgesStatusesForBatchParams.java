package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("UpdateBadgesStatusesForBatchParams")
@Data
@Builder
public class UpdateBadgesStatusesForBatchParams {
  Integer batchId;
  String status;
}
