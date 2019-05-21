package uk.gov.dft.bluebadge.model.badgemanagement.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Badge */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Badge {
  @JsonProperty("badgeNumber")
  private String badgeNumber;

  @JsonProperty("party")
  private Party party;

  @JsonProperty("localAuthorityShortCode")
  private String localAuthorityShortCode;

  @JsonProperty("localAuthorityRef")
  private String localAuthorityRef;

  @JsonProperty("applicationDate")
  private LocalDate applicationDate;

  @JsonProperty("applicationChannelCode")
  private String applicationChannelCode;

  @JsonProperty("orderDate")
  private LocalDate orderDate;

  @JsonProperty("issuedDate")
  private LocalDateTime issuedDate;

  @JsonProperty("printRequestDateTime")
  private LocalDateTime printRequestDateTime;

  @JsonProperty("startDate")
  private LocalDate startDate;

  @JsonProperty("expiryDate")
  private LocalDate expiryDate;

  @JsonProperty("eligibilityCode")
  private String eligibilityCode;

  @JsonProperty("notForReassessment")
  private Boolean notForReassessment;

  @JsonProperty("imageLink")
  private String imageLink;

  @JsonProperty("cancelReasonCode")
  private String cancelReasonCode;

  @JsonProperty("replaceReasonCode")
  private String replaceReasonCode;

  @JsonProperty("rejectedReason")
  private String rejectedReason;

  @JsonProperty("statusCode")
  private String statusCode;
}
