package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import java.io.Serializable;
import org.apache.ibatis.type.Alias;

/** Bean to hold a UserEntity record. */
@Alias("UserEntity")
public class BadgeEntity implements Serializable {
  private static final long serialVersionUID = 1L;
}
