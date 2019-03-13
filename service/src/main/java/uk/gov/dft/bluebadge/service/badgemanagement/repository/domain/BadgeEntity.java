package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import uk.gov.dft.bluebadge.common.service.enums.EligibilityType;

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
    DELETED,
    ORDERED,
    PROCESSED,
    REJECT
  }

  private String badgeNo;
  private Status badgeStatus;
  private String partyCode;
  private String localAuthorityShortCode;
  private String localAuthorityRef;
  private LocalDate appDate;
  private String appChannelCode;
  private LocalDate startDate;
  private LocalDate expiryDate;
  private EligibilityType eligibilityCode;
  private String imageLink;
  private String imageLinkOriginal;
  private String deliverToCode;
  private String deliverOptionCode;
  private String holderName;
  private String nino;
  private LocalDate dob;
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
  private LocalDate orderDate;
  private int numberOfBadges;
  private byte[] badgeHash;
  private String rejectedReason;
  private LocalDateTime issuedDate;
  private LocalDateTime printRequestDateTime;

  public boolean isPerson() {
    return "PERSON".equals(partyCode);
  }
}
