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
    "holderName",
    "nino",
    "dob",
    "genderCode",
    "contactName",
    "contactBuildingStreet",
    "contactLine2",
    "contactTownCity",
    "contactPostcode",
    "primaryPhoneNo",
    "secondaryPhoneNo",
    "contactEmailAddress",
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
  private String deliverToCode;
  private String deliverOptionCode;
  private String holderName;
  private String nino;
  private String dob;
  private String genderCode;
  private String contactName;
  private String contactBuildingStreet;
  private String contactLine2;
  private String contactTownCity;
  private String contactPostcode;
  private String primaryPhoneNo;
  private String secondaryPhoneNo;
  private String contactEmailAddress;
  private String cancelReasonCode;
  private String replaceReasonCode;
  private String orderDate;
  private String rejectedReason;
  private String rejectedDateTime;
  private String issuedDateTime;
  private String printRequestDateTime;
}
