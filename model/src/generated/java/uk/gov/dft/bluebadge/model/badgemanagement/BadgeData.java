package uk.gov.dft.bluebadge.model.badgemanagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

/** BadgeData */
@Validated
public class BadgeData extends Data {
  @JsonProperty("badge")
  private Badge badge = null;

  public BadgeData badge(Badge badge) {
    this.badge = badge;
    return this;
  }

  /**
   * Get badge
   *
   * @return badge
   */
  @ApiModelProperty(value = "")
  @Valid
  public Badge getBadge() {
    return badge;
  }

  public void setBadge(Badge badge) {
    this.badge = badge;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BadgeData badgeData = (BadgeData) o;
    return Objects.equals(this.badge, badgeData.badge) && super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(badge, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BadgeData {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    badge: ").append(toIndentedString(badge)).append("\n");
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
