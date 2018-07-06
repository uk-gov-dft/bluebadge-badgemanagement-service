package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("FindBadgeParams")
@Data
@Builder
public class FindBadgeParams {
  String name;
  String postcode;
}
