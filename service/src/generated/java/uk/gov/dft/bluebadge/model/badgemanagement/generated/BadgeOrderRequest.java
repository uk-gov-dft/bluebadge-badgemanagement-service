package uk.gov.dft.bluebadge.model.badgemanagement.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.dft.bluebadge.common.service.enums.EligibilityType;
import uk.gov.dft.bluebadge.common.util.ValidationPattern;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BadgeOrderRequest {
  @JsonProperty("party")
  @NotNull
  @Valid
  private Party party;

  @JsonProperty("localAuthorityShortCode")
  @Valid
  @Pattern(regexp = ValidationPattern.LA_SHORT_CODE)
  private String localAuthorityShortCode;

  @JsonProperty("localAuthorityRef")
  @Size(max = 100)
  private String localAuthorityRef;

  @JsonProperty("applicationDate")
  @NotNull
  private LocalDate applicationDate;

  @JsonProperty("applicationChannelCode")
  @NotNull
  @Size(max = 10)
  private String applicationChannelCode;

  @JsonProperty("startDate")
  @NotNull
  private LocalDate startDate;

  @JsonProperty("expiryDate")
  @NotNull
  private LocalDate expiryDate;

  @JsonProperty("eligibilityCode")
  @NotNull
  private EligibilityType eligibilityCode;

  @JsonProperty("notForReassessment")
  private Boolean notForReassessment;

  @JsonProperty("imageFile")
  private String imageFile;

  @JsonProperty("deliverToCode")
  @NotNull
  @Size(max = 10)
  private String deliverToCode;

  @JsonProperty("deliveryOptionCode")
  @NotNull
  @Size(max = 10)
  private String deliveryOptionCode;

  @JsonProperty("numberOfBadges")
  @NotNull
  @Valid
  @Min(1)
  @Max(999)
  private Integer numberOfBadges;
}
