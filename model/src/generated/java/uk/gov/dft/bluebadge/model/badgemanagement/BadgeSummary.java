package uk.gov.dft.bluebadge.model.badgemanagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

/** BadgeSummary */
@Validated
public class BadgeSummary {
  @JsonProperty("badgeNumber")
  private String badgeNumber = null;

  @JsonProperty("partyType")
  private PartyTypeField partyType = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("nationalInsurance")
  private String nationalInsurance = null;

  @JsonProperty("localAuthorityName")
  private String localAuthorityName = null;

  @JsonProperty("expiryDate")
  private String expiryDate = null;

  @JsonProperty("status")
  private StatusField status = null;

  public BadgeSummary badgeNumber(String badgeNumber) {
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

  public BadgeSummary partyType(PartyTypeField partyType) {
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

  public BadgeSummary name(String name) {
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

  public BadgeSummary nationalInsurance(String nationalInsurance) {
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

  public BadgeSummary localAuthorityName(String localAuthorityName) {
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

  public BadgeSummary expiryDate(String expiryDate) {
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

  public BadgeSummary status(StatusField status) {
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
    BadgeSummary badgeSummary = (BadgeSummary) o;
    return Objects.equals(this.badgeNumber, badgeSummary.badgeNumber)
        && Objects.equals(this.partyType, badgeSummary.partyType)
        && Objects.equals(this.name, badgeSummary.name)
        && Objects.equals(this.nationalInsurance, badgeSummary.nationalInsurance)
        && Objects.equals(this.localAuthorityName, badgeSummary.localAuthorityName)
        && Objects.equals(this.expiryDate, badgeSummary.expiryDate)
        && Objects.equals(this.status, badgeSummary.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        badgeNumber, partyType, name, nationalInsurance, localAuthorityName, expiryDate, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BadgeSummary {\n");

    sb.append("    badgeNumber: ").append(toIndentedString(badgeNumber)).append("\n");
    sb.append("    partyType: ").append(toIndentedString(partyType)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    nationalInsurance: ").append(toIndentedString(nationalInsurance)).append("\n");
    sb.append("    localAuthorityName: ").append(toIndentedString(localAuthorityName)).append("\n");
    sb.append("    expiryDate: ").append(toIndentedString(expiryDate)).append("\n");
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
