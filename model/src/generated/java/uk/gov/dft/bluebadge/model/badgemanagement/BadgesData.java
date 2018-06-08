package uk.gov.dft.bluebadge.model.badgemanagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

/** BadgesData */
@Validated
public class BadgesData extends Data {
  @JsonProperty("badges")
  @Valid
  private List<BadgeSummary> badges = null;

  public BadgesData badges(List<BadgeSummary> badges) {
    this.badges = badges;
    return this;
  }

  public BadgesData addBadgesItem(BadgeSummary badgesItem) {
    if (this.badges == null) {
      this.badges = new ArrayList<>();
    }
    this.badges.add(badgesItem);
    return this;
  }

  /**
   * Get badges
   *
   * @return badges
   */
  @ApiModelProperty(value = "")
  @Valid
  public List<BadgeSummary> getBadges() {
    return badges;
  }

  public void setBadges(List<BadgeSummary> badges) {
    this.badges = badges;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BadgesData badgesData = (BadgesData) o;
    return Objects.equals(this.badges, badgesData.badges) && super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(badges, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BadgesData {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    badges: ").append(toIndentedString(badges)).append("\n");
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
