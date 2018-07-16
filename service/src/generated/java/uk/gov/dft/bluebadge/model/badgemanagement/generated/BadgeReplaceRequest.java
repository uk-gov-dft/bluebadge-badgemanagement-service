package uk.gov.dft.bluebadge.model.badgemanagement.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

/** BadgeReplaceRequest */
@Validated
public class BadgeReplaceRequest {
  @JsonProperty("badgeNumber")
  private String badgeNumber = null;

  @JsonProperty("replaceReasonCode")
  private String replaceReasonCode = null;

  @JsonProperty("deliverToCode")
  private String deliverToCode = null;

  @JsonProperty("deliveryOptionCode")
  private String deliveryOptionCode = null;

  public BadgeReplaceRequest badgeNumber(String badgeNumber) {
    this.badgeNumber = badgeNumber;
    return this;
  }

  /**
   * The unique badge number for this badge.
   *
   * @return badgeNumber
   */
  @ApiModelProperty(
    example = "72B9HA",
    required = true,
    value = "The unique badge number for this badge."
  )
  @NotNull
  @Pattern(regexp = "^[0-9A-HJK]{6}$")
  public String getBadgeNumber() {
    return badgeNumber;
  }

  public void setBadgeNumber(String badgeNumber) {
    this.badgeNumber = badgeNumber;
  }

  public BadgeReplaceRequest replaceReasonCode(String replaceReasonCode) {
    this.replaceReasonCode = replaceReasonCode;
    return this;
  }

  /**
   * A short code from the REPLACE group of reference data.
   *
   * @return replaceReasonCode
   */
  @ApiModelProperty(
    example = "STOLEN",
    required = true,
    value = "A short code from the REPLACE group of reference data."
  )
  @NotNull
  @Size(max = 10)
  public String getReplaceReasonCode() {
    return replaceReasonCode;
  }

  public void setReplaceReasonCode(String replaceReasonCode) {
    this.replaceReasonCode = replaceReasonCode;
  }

  public BadgeReplaceRequest deliverToCode(String deliverToCode) {
    this.deliverToCode = deliverToCode;
    return this;
  }

  /**
   * A short code from the DELIVER group of reference data.
   *
   * @return deliverToCode
   */
  @ApiModelProperty(
    example = "HOME",
    required = true,
    value = "A short code from the DELIVER group of reference data."
  )
  @NotNull
  @Size(max = 10)
  public String getDeliverToCode() {
    return deliverToCode;
  }

  public void setDeliverToCode(String deliverToCode) {
    this.deliverToCode = deliverToCode;
  }

  public BadgeReplaceRequest deliveryOptionCode(String deliveryOptionCode) {
    this.deliveryOptionCode = deliveryOptionCode;
    return this;
  }

  /**
   * A short code from the DELROPT group of reference data.
   *
   * @return deliveryOptionCode
   */
  @ApiModelProperty(
    example = "STANDARD",
    required = true,
    value = "A short code from the DELROPT group of reference data."
  )
  @NotNull
  @Size(max = 10)
  public String getDeliveryOptionCode() {
    return deliveryOptionCode;
  }

  public void setDeliveryOptionCode(String deliveryOptionCode) {
    this.deliveryOptionCode = deliveryOptionCode;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BadgeReplaceRequest badgeReplaceRequest = (BadgeReplaceRequest) o;
    return Objects.equals(this.badgeNumber, badgeReplaceRequest.badgeNumber)
        && Objects.equals(this.replaceReasonCode, badgeReplaceRequest.replaceReasonCode)
        && Objects.equals(this.deliverToCode, badgeReplaceRequest.deliverToCode)
        && Objects.equals(this.deliveryOptionCode, badgeReplaceRequest.deliveryOptionCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(badgeNumber, replaceReasonCode, deliverToCode, deliveryOptionCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BadgeReplaceRequest {\n");

    sb.append("    badgeNumber: ").append(toIndentedString(badgeNumber)).append("\n");
    sb.append("    replaceReasonCode: ").append(toIndentedString(replaceReasonCode)).append("\n");
    sb.append("    deliverToCode: ").append(toIndentedString(deliverToCode)).append("\n");
    sb.append("    deliveryOptionCode: ").append(toIndentedString(deliveryOptionCode)).append("\n");
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
