package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("FindBadgesForPrintBatchParams")
@Data
@Builder
public class FindBadgesForPrintBatchParams {
  String batchType;
}
