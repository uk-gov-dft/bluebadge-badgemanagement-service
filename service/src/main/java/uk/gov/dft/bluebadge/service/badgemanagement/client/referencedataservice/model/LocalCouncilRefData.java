package uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocalCouncilRefData extends ReferenceData {
  @JsonProperty("metaData")
  private Optional<LocalCouncilMetaData> localCouncilMetaData;

  @JsonIgnore
  public String getIssuingAuthorityShortCode() {
    return localCouncilMetaData
        .map(LocalCouncilMetaData::getIssuingAuthorityShortCode)
        .orElse(null);
  }

  @Data
  public static class LocalCouncilMetaData {
    private String issuingAuthorityShortCode;
  }
}
