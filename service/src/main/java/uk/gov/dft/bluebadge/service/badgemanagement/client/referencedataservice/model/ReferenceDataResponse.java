package uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model;

import lombok.Data;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

import java.util.List;

@Data
public class ReferenceDataResponse extends CommonResponse {
  private List<ReferenceData> data;
}
