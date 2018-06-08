package uk.gov.dft.bluebadge.model.badgemanagement;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * BadgeCancelRequest
 */
@Validated

public class BadgeCancelRequest   {
  @JsonProperty("badgeNumber")
  private String badgeNumber = null;

  @JsonProperty("cancelReasonCode")
  private String cancelReasonCode = null;

  public BadgeCancelRequest badgeNumber(String badgeNumber) {
    this.badgeNumber = badgeNumber;
    return this;
  }

  /**
   * The unique badge number for this badge
   * @return badgeNumber
  **/
  @ApiModelProperty(example = "095715", required = true, value = "The unique badge number for this badge")
  @NotNull


  public String getBadgeNumber() {
    return badgeNumber;
  }

  public void setBadgeNumber(String badgeNumber) {
    this.badgeNumber = badgeNumber;
  }

  public BadgeCancelRequest cancelReasonCode(String cancelReasonCode) {
    this.cancelReasonCode = cancelReasonCode;
    return this;
  }

  /**
   * Code for Deceased, No longer needed or Revoked 
   * @return cancelReasonCode
  **/
  @ApiModelProperty(required = true, value = "Code for Deceased, No longer needed or Revoked ")
  @NotNull

@Size(max=10) 
  public String getCancelReasonCode() {
    return cancelReasonCode;
  }

  public void setCancelReasonCode(String cancelReasonCode) {
    this.cancelReasonCode = cancelReasonCode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BadgeCancelRequest badgeCancelRequest = (BadgeCancelRequest) o;
    return Objects.equals(this.badgeNumber, badgeCancelRequest.badgeNumber) &&
        Objects.equals(this.cancelReasonCode, badgeCancelRequest.cancelReasonCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(badgeNumber, cancelReasonCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BadgeCancelRequest {\n");
    
    sb.append("    badgeNumber: ").append(toIndentedString(badgeNumber)).append("\n");
    sb.append("    cancelReasonCode: ").append(toIndentedString(cancelReasonCode)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

