package uk.gov.dft.bluebadge.model.badgemanagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

/** HomeAddressField */
@Validated
public class HomeAddressField {
  @JsonProperty("buildingStreet")
  private String buildingStreet = null;

  @JsonProperty("line2")
  private String line2 = null;

  @JsonProperty("townCity")
  private String townCity = null;

  @JsonProperty("postCode")
  private String postCode = null;

  public HomeAddressField buildingStreet(String buildingStreet) {
    this.buildingStreet = buildingStreet;
    return this;
  }

  /**
   * Get buildingStreet
   *
   * @return buildingStreet
   */
  @ApiModelProperty(example = "65 Basil Chambers", value = "")
  public String getBuildingStreet() {
    return buildingStreet;
  }

  public void setBuildingStreet(String buildingStreet) {
    this.buildingStreet = buildingStreet;
  }

  public HomeAddressField line2(String line2) {
    this.line2 = line2;
    return this;
  }

  /**
   * Get line2
   *
   * @return line2
   */
  @ApiModelProperty(example = "Northern Quarter", value = "")
  public String getLine2() {
    return line2;
  }

  public void setLine2(String line2) {
    this.line2 = line2;
  }

  public HomeAddressField townCity(String townCity) {
    this.townCity = townCity;
    return this;
  }

  /**
   * Get townCity
   *
   * @return townCity
   */
  @ApiModelProperty(example = "Manchester", value = "")
  public String getTownCity() {
    return townCity;
  }

  public void setTownCity(String townCity) {
    this.townCity = townCity;
  }

  public HomeAddressField postCode(String postCode) {
    this.postCode = postCode;
    return this;
  }

  /**
   * Get postCode
   *
   * @return postCode
   */
  @ApiModelProperty(example = "SK6 8GH", value = "")
  public String getPostCode() {
    return postCode;
  }

  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HomeAddressField homeAddressField = (HomeAddressField) o;
    return Objects.equals(this.buildingStreet, homeAddressField.buildingStreet)
        && Objects.equals(this.line2, homeAddressField.line2)
        && Objects.equals(this.townCity, homeAddressField.townCity)
        && Objects.equals(this.postCode, homeAddressField.postCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(buildingStreet, line2, townCity, postCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HomeAddressField {\n");

    sb.append("    buildingStreet: ").append(toIndentedString(buildingStreet)).append("\n");
    sb.append("    line2: ").append(toIndentedString(line2)).append("\n");
    sb.append("    townCity: ").append(toIndentedString(townCity)).append("\n");
    sb.append("    postCode: ").append(toIndentedString(postCode)).append("\n");
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
