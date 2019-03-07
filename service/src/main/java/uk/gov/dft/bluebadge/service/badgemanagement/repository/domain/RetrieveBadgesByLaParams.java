package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RetrieveBadgesByLaParams {
  private String laShortCode;
}
