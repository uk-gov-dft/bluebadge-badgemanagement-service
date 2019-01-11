package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import uk.gov.dft.bluebadge.service.badgemanagement.model.BatchType;

@Alias("AppendBadgesToBatchParams")
@Data
@Builder
public class AppendBadgesToBatchParams {
  Integer batchId;
  BatchType batchType;
}
