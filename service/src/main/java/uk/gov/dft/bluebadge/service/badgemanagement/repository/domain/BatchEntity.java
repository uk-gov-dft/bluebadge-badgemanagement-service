package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/** Bean to hold a BatchEntity record. */
@Alias("BatchEntity")
@Data
@Builder
public class BatchEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  String id;
  String filename;
  LocalDateTime created;
  String source;
  String purpose;
}
