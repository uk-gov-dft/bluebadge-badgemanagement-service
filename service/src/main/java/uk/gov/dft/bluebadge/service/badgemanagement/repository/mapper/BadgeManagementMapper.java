package uk.gov.dft.bluebadge.service.badgemanagement.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;

@SuppressWarnings("unused")
@Mapper
public interface BadgeManagementMapper {
  /**
   * Create a badge.
   *
   * @param badgeEntity badge to create with badge number already set.
   */
  void createBadge(BadgeEntity badgeEntity);

  /**
   * Retrieve badge number from DB sequence.
   *
   * @return The next badge number.
   */
  Integer retrieveNextBadgeNumber();
}
