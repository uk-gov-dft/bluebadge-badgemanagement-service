package uk.gov.dft.bluebadge.service.badgemanagement.service;

import lombok.Data;

@Data
class S3KeyNames {
  private String originalKeyName;
  private String thumbnailKeyName;
}
