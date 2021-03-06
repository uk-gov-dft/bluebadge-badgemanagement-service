package uk.gov.dft.bluebadge.model.badgemanagement.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

/** BadgeResponse */
@Validated
public class BadgeResponse extends CommonResponse {
  @JsonProperty("data")
  private Badge data = null;

  public BadgeResponse data(Badge data) {
    this.data = data;
    return this;
  }

  /**
   * Get data
   *
   * @return data
   */
  @ApiModelProperty(value = "")
  @Valid
  public Badge getData() {
    return data;
  }

  public void setData(Badge data) {
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
    BadgeResponse badgeResponse = (BadgeResponse) o;
    return Objects.equals(this.data, badgeResponse.data) && super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BadgeResponse {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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
