package uk.gov.dft.bluebadge.model.badgemanagement.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import uk.gov.dft.bluebadge.common.service.enums.EligibilityType;
import uk.gov.dft.bluebadge.common.util.ValidationPattern;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Party;

/** BadgeOrderRequest */
@Validated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadgeOrderRequest {
  @JsonProperty("party")
  @NotNull
  private Party party = null;

  @JsonProperty("localAuthorityShortCode")
  @Pattern(regexp = ValidationPattern.LA_SHORT_CODE)
  private String localAuthorityShortCode = null;

  @JsonProperty("localAuthorityRef")
  @Size(max = 100)
  private String localAuthorityRef = null;

  @JsonProperty("applicationDate")
  @NotNull
  @Valid
  private LocalDate applicationDate = null;

  @JsonProperty("applicationChannelCode")
  @NotNull
  @Size(max = 10)
  private String applicationChannelCode = null;

  @JsonProperty("startDate")
  @NotNull
  @Valid
  private LocalDate startDate = null;

  @JsonProperty("expiryDate")
  @NotNull
  @Valid
  private LocalDate expiryDate = null;

  @JsonProperty("eligibilityCode")
  @NotNull
  private EligibilityType eligibilityCode = null;

  @JsonProperty("notForReassessment")
  private Boolean notForReassessment = null;

  @JsonProperty("imageFile")
  private String imageFile = null;

  @JsonProperty("deliverToCode")
  @NotNull
  @Size(max = 10)
  private String deliverToCode = null;

  @JsonProperty("deliveryOptionCode")
  @NotNull
  @Size(max = 10)
  private String deliveryOptionCode = null;

  @JsonProperty("numberOfBadges")
  @Min(1)
  @Max(999)
  private Integer numberOfBadges = null;
}
