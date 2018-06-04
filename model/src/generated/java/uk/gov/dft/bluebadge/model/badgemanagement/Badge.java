package uk.gov.dft.bluebadge.model.badgemanagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

/** Badge */
@Validated
public class Badge {
  @JsonProperty("badgeNumber")
  private String badgeNumber = null;

  @JsonProperty("partyType")
  private PartyTypeField partyType = null;

  @JsonProperty("nationalInsurance")
  private String nationalInsurance = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("homeAddress")
  private HomeAddressField homeAddress = null;

  @JsonProperty("contactNumber")
  private String contactNumber = null;

  @JsonProperty("emailAddress")
  private String emailAddress = null;

  @JsonProperty("dob")
  private String dob = null;

  @JsonProperty("localAuthorityName")
  private String localAuthorityName = null;

  @JsonProperty("localAuthorityId")
  private Integer localAuthorityId = null;

  @JsonProperty("localAuthorityRef")
  private String localAuthorityRef = null;

  @JsonProperty("applicationDate")
  private String applicationDate = null;

  @JsonProperty("applicationSource")
  private ApplicationSourceField applicationSource = null;

  @JsonProperty("orderDate")
  private String orderDate = null;

  @JsonProperty("startDate")
  private String startDate = null;

  @JsonProperty("expiryDate")
  private String expiryDate = null;

  @JsonProperty("eligibility")
  private EligibilityField eligibility = null;

  @JsonProperty("thumbNail")
  private String thumbNail = null;

  @JsonProperty("status")
  private StatusField status = null;

  public Badge badgeNumber(String badgeNumber) {
    this.badgeNumber = badgeNumber;
    return this;
  }

  /**
   * Get badgeNumber
   *
   * @return badgeNumber
   */
  @ApiModelProperty(value = "")
  public String getBadgeNumber() {
    return badgeNumber;
  }

  public void setBadgeNumber(String badgeNumber) {
    this.badgeNumber = badgeNumber;
  }

  public Badge partyType(PartyTypeField partyType) {
    this.partyType = partyType;
    return this;
  }

  /**
   * Get partyType
   *
   * @return partyType
   */
  @ApiModelProperty(value = "")
  @Valid
  public PartyTypeField getPartyType() {
    return partyType;
  }

  public void setPartyType(PartyTypeField partyType) {
    this.partyType = partyType;
  }

  public Badge nationalInsurance(String nationalInsurance) {
    this.nationalInsurance = nationalInsurance;
    return this;
  }

  /**
   * Get nationalInsurance
   *
   * @return nationalInsurance
   */
  @ApiModelProperty(value = "")
  public String getNationalInsurance() {
    return nationalInsurance;
  }

  public void setNationalInsurance(String nationalInsurance) {
    this.nationalInsurance = nationalInsurance;
  }

  public Badge name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   *
   * @return name
   */
  @ApiModelProperty(value = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Badge homeAddress(HomeAddressField homeAddress) {
    this.homeAddress = homeAddress;
    return this;
  }

  /**
   * Get homeAddress
   *
   * @return homeAddress
   */
  @ApiModelProperty(value = "")
  @Valid
  public HomeAddressField getHomeAddress() {
    return homeAddress;
  }

  public void setHomeAddress(HomeAddressField homeAddress) {
    this.homeAddress = homeAddress;
  }

  public Badge contactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
    return this;
  }

  /**
   * Get contactNumber
   *
   * @return contactNumber
   */
  @ApiModelProperty(value = "")
  public String getContactNumber() {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }

  public Badge emailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  /**
   * Get emailAddress
   *
   * @return emailAddress
   */
  @ApiModelProperty(value = "")
  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public Badge dob(String dob) {
    this.dob = dob;
    return this;
  }

  /**
   * Get dob
   *
   * @return dob
   */
  @ApiModelProperty(value = "")
  public String getDob() {
    return dob;
  }

  public void setDob(String dob) {
    this.dob = dob;
  }

  public Badge localAuthorityName(String localAuthorityName) {
    this.localAuthorityName = localAuthorityName;
    return this;
  }

  /**
   * Get localAuthorityName
   *
   * @return localAuthorityName
   */
  @ApiModelProperty(value = "")
  public String getLocalAuthorityName() {
    return localAuthorityName;
  }

  public void setLocalAuthorityName(String localAuthorityName) {
    this.localAuthorityName = localAuthorityName;
  }

  public Badge localAuthorityId(Integer localAuthorityId) {
    this.localAuthorityId = localAuthorityId;
    return this;
  }

  /**
   * Get localAuthorityId
   *
   * @return localAuthorityId
   */
  @ApiModelProperty(value = "")
  public Integer getLocalAuthorityId() {
    return localAuthorityId;
  }

  public void setLocalAuthorityId(Integer localAuthorityId) {
    this.localAuthorityId = localAuthorityId;
  }

  public Badge localAuthorityRef(String localAuthorityRef) {
    this.localAuthorityRef = localAuthorityRef;
    return this;
  }

  /**
   * Get localAuthorityRef
   *
   * @return localAuthorityRef
   */
  @ApiModelProperty(value = "")
  public String getLocalAuthorityRef() {
    return localAuthorityRef;
  }

  public void setLocalAuthorityRef(String localAuthorityRef) {
    this.localAuthorityRef = localAuthorityRef;
  }

  public Badge applicationDate(String applicationDate) {
    this.applicationDate = applicationDate;
    return this;
  }

  /**
   * Get applicationDate
   *
   * @return applicationDate
   */
  @ApiModelProperty(value = "")
  public String getApplicationDate() {
    return applicationDate;
  }

  public void setApplicationDate(String applicationDate) {
    this.applicationDate = applicationDate;
  }

  public Badge applicationSource(ApplicationSourceField applicationSource) {
    this.applicationSource = applicationSource;
    return this;
  }

  /**
   * Get applicationSource
   *
   * @return applicationSource
   */
  @ApiModelProperty(value = "")
  @Valid
  public ApplicationSourceField getApplicationSource() {
    return applicationSource;
  }

  public void setApplicationSource(ApplicationSourceField applicationSource) {
    this.applicationSource = applicationSource;
  }

  public Badge orderDate(String orderDate) {
    this.orderDate = orderDate;
    return this;
  }

  /**
   * Get orderDate
   *
   * @return orderDate
   */
  @ApiModelProperty(value = "")
  public String getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(String orderDate) {
    this.orderDate = orderDate;
  }

  public Badge startDate(String startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Get startDate
   *
   * @return startDate
   */
  @ApiModelProperty(value = "")
  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public Badge expiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
    return this;
  }

  /**
   * Get expiryDate
   *
   * @return expiryDate
   */
  @ApiModelProperty(value = "")
  public String getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
  }

  public Badge eligibility(EligibilityField eligibility) {
    this.eligibility = eligibility;
    return this;
  }

  /**
   * Get eligibility
   *
   * @return eligibility
   */
  @ApiModelProperty(value = "")
  @Valid
  public EligibilityField getEligibility() {
    return eligibility;
  }

  public void setEligibility(EligibilityField eligibility) {
    this.eligibility = eligibility;
  }

  public Badge thumbNail(String thumbNail) {
    this.thumbNail = thumbNail;
    return this;
  }

  /**
   * Get thumbNail
   *
   * @return thumbNail
   */
  @ApiModelProperty(value = "")
  public String getThumbNail() {
    return thumbNail;
  }

  public void setThumbNail(String thumbNail) {
    this.thumbNail = thumbNail;
  }

  public Badge status(StatusField status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   *
   * @return status
   */
  @ApiModelProperty(value = "")
  @Valid
  public StatusField getStatus() {
    return status;
  }

  public void setStatus(StatusField status) {
    this.status = status;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Badge badge = (Badge) o;
    return Objects.equals(this.badgeNumber, badge.badgeNumber)
        && Objects.equals(this.partyType, badge.partyType)
        && Objects.equals(this.nationalInsurance, badge.nationalInsurance)
        && Objects.equals(this.name, badge.name)
        && Objects.equals(this.homeAddress, badge.homeAddress)
        && Objects.equals(this.contactNumber, badge.contactNumber)
        && Objects.equals(this.emailAddress, badge.emailAddress)
        && Objects.equals(this.dob, badge.dob)
        && Objects.equals(this.localAuthorityName, badge.localAuthorityName)
        && Objects.equals(this.localAuthorityId, badge.localAuthorityId)
        && Objects.equals(this.localAuthorityRef, badge.localAuthorityRef)
        && Objects.equals(this.applicationDate, badge.applicationDate)
        && Objects.equals(this.applicationSource, badge.applicationSource)
        && Objects.equals(this.orderDate, badge.orderDate)
        && Objects.equals(this.startDate, badge.startDate)
        && Objects.equals(this.expiryDate, badge.expiryDate)
        && Objects.equals(this.eligibility, badge.eligibility)
        && Objects.equals(this.thumbNail, badge.thumbNail)
        && Objects.equals(this.status, badge.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        badgeNumber,
        partyType,
        nationalInsurance,
        name,
        homeAddress,
        contactNumber,
        emailAddress,
        dob,
        localAuthorityName,
        localAuthorityId,
        localAuthorityRef,
        applicationDate,
        applicationSource,
        orderDate,
        startDate,
        expiryDate,
        eligibility,
        thumbNail,
        status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Badge {\n");

    sb.append("    badgeNumber: ").append(toIndentedString(badgeNumber)).append("\n");
    sb.append("    partyType: ").append(toIndentedString(partyType)).append("\n");
    sb.append("    nationalInsurance: ").append(toIndentedString(nationalInsurance)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    homeAddress: ").append(toIndentedString(homeAddress)).append("\n");
    sb.append("    contactNumber: ").append(toIndentedString(contactNumber)).append("\n");
    sb.append("    emailAddress: ").append(toIndentedString(emailAddress)).append("\n");
    sb.append("    dob: ").append(toIndentedString(dob)).append("\n");
    sb.append("    localAuthorityName: ").append(toIndentedString(localAuthorityName)).append("\n");
    sb.append("    localAuthorityId: ").append(toIndentedString(localAuthorityId)).append("\n");
    sb.append("    localAuthorityRef: ").append(toIndentedString(localAuthorityRef)).append("\n");
    sb.append("    applicationDate: ").append(toIndentedString(applicationDate)).append("\n");
    sb.append("    applicationSource: ").append(toIndentedString(applicationSource)).append("\n");
    sb.append("    orderDate: ").append(toIndentedString(orderDate)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    expiryDate: ").append(toIndentedString(expiryDate)).append("\n");
    sb.append("    eligibility: ").append(toIndentedString(eligibility)).append("\n");
    sb.append("    thumbNail: ").append(toIndentedString(thumbNail)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
