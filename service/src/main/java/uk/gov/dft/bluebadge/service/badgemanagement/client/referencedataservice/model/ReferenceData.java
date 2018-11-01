package uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@JsonTypeInfo(
  defaultImpl = ReferenceData.class,
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "groupShortCode",
  visible = true
)
@JsonSubTypes({
  @JsonSubTypes.Type(value = LocalAuthorityRefData.class, name = "LA"),
  @JsonSubTypes.Type(value = LocalCouncilRefData.class, name = "LC")
})
public class ReferenceData {
  private String shortCode;
  private String description;
  private Map<String, Object> metaData;
  private String groupShortCode;
  private String groupDescription;
  private String subgroupShortCode;
  private String subgroupDescription;
  private Integer displayOrder;
}
