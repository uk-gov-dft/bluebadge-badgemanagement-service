package uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model;

import java.util.List;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

public class ReferenceDataResponse extends CommonResponse {
  private List<ReferenceData> data;

  public List<ReferenceData> getData() {
    return data;
  }

  public void setData(List<ReferenceData> data) {
    this.data = data;
  }
}
