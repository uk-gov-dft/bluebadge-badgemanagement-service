package uk.gov.dft.bluebadge.service.badgemanagement.model;

import java.util.List;
import lombok.Data;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

@Data
public class IssuedBadgesReportResponse extends CommonResponse {
  private List<IssuedBadge> data;
}
