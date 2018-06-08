package uk.gov.dft.bluebadge.model.badgemanagement;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import uk.gov.dft.bluebadge.model.badgemanagement.CommonResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.Error;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Contains a list of Badge numbers.
 */
@ApiModel(description = "Contains a list of Badge numbers.")
@Validated

public class BadgeNumbersResponse extends CommonResponse  {
  @JsonProperty("data")
  @Valid
  private List<String> data = null;

  public BadgeNumbersResponse data(List<String> data) {
    this.data = data;
    return this;
  }

  public BadgeNumbersResponse addDataItem(String dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * Get data
   * @return data
  **/
  @ApiModelProperty(value = "")


  public List<String> getData() {
    return data;
  }

  public void setData(List<String> data) {
    this.data = data;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BadgeNumbersResponse badgeNumbersResponse = (BadgeNumbersResponse) o;
    return Objects.equals(this.data, badgeNumbersResponse.data) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BadgeNumbersResponse {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

