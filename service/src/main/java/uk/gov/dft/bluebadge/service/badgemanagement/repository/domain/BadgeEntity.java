package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/** Bean to hold a BadgeEntity record. */
@Alias("BadgeEntity")
@Data
@Builder
public class BadgeEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unused")
  public enum Status {
    ISSUED,
    CANCELLED,
    REPLACED,
    DELETED
  }

  String badgeNo;
  Status badgeStatus;
  String partyCode;
  String localAuthorityShortCode;
  String localAuthorityRef;
  LocalDate appDate;
  String appChannelCode;
  LocalDate startDate;
  LocalDate expiryDate;
  String eligibilityCode;
  String imageLink;
  String imageLinkOriginal;
  String deliverToCode;
  String deliverOptionCode;
  String holderName;
  String nino;
  LocalDate dob;
  String genderCode;
  String contactName;
  String contactBuildingStreet;
  String contactLine2;
  String contactTownCity;
  String contactPostcode;
  String primaryPhoneNo;
  String secondaryPhoneNo;
  String contactEmailAddress;
  String cancelReasonCode;
  String replaceReasonCode;
  LocalDate orderDate;
  int numberOfBadges;

  public boolean isPerson() {
    return "PERSON".equals(partyCode);
  }
}
