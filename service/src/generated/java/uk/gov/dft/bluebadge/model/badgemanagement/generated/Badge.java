package uk.gov.dft.bluebadge.model.badgemanagement.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Party;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Badge
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Badge {
  @JsonProperty("badgeNumber")
  private String badgeNumber = null;

  @JsonProperty("party")
  private Party party = null;

  @JsonProperty("localAuthorityShortCode")
  private String localAuthorityShortCode = null;

  @JsonProperty("localAuthorityRef")
  private String localAuthorityRef = null;

  @JsonProperty("applicationDate")
  private LocalDate applicationDate = null;

  @JsonProperty("applicationChannelCode")
  private String applicationChannelCode = null;

  @JsonProperty("orderDate")
  private LocalDate orderDate = null;

  @JsonProperty("issuedDate")
  private LocalDateTime issuedDate = null;

  @JsonProperty("printRequestDateTime")
  private LocalDateTime printRequestDateTime = null;

  @JsonProperty("startDate")
  private LocalDate startDate = null;

  @JsonProperty("expiryDate")
  private LocalDate expiryDate = null;

  @JsonProperty("eligibilityCode")
  private String eligibilityCode = null;

  @JsonProperty("notForReassessment")
  private Boolean notForReassessment = null;

  @JsonProperty("imageLink")
  private String imageLink = null;

  @JsonProperty("cancelReasonCode")
  private String cancelReasonCode = null;

  @JsonProperty("replaceReasonCode")
  private String replaceReasonCode = null;

  @JsonProperty("rejectedReason")
  private String rejectedReason;

  @JsonProperty("statusCode")
  private String statusCode = null;
}
