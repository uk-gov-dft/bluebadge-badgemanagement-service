package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import uk.gov.dft.bluebadge.common.service.enums.EligibilityType;

@Builder
@Data
@JsonPropertyOrder(
  value = {
    "badgeNo",
    "badgeStatus",
    "partyCode",
    "localAuthorityShortCode",
    "localAuthorityRef",
    "appDate",
    "appChannelCode",
    "startDate",
    "expiryDate",
    "eligibilityCode",
    "deliverToCode",
    "deliverOptionCode",
    "cancelReasonCode",
    "replaceReasonCode",
    "orderDate",
    "rejectedReason",
    "rejectedDateTime",
    "issuedDateTime",
    "printRequestDateTime"
  }
)
public class BadgeZipEntity {
  private String badgeNo;
  private BadgeEntity.Status badgeStatus;
  private String partyCode;
  private String localAuthorityShortCode;
  private String localAuthorityRef;
  private String appDate;
  private String appChannelCode;
  private String startDate;
  private String expiryDate;
  private EligibilityType eligibilityCode;
  private Boolean notForReassessment;
  private String deliverToCode;
  private String deliverOptionCode;
  private String cancelReasonCode;
  private String replaceReasonCode;
  private String orderDate;
  private String rejectedReason;
  private String rejectedDateTime;
  private String issuedDateTime;
  private String printRequestDateTime;
}
