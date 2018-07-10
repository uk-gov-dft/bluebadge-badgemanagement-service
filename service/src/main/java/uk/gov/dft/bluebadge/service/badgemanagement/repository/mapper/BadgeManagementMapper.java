package uk.gov.dft.bluebadge.service.badgemanagement.repository.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.RetrieveBadgeParams;

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

  /**
   * Retrieve list of badges.
   *
   * @param params Object holding search criteria.
   * @return List of BadgeEntity
   */
  List<BadgeEntity> findBadges(FindBadgeParams params);

  /**
   * Retrieve a single badge
   *
   * @param params Holds badge number to retrieve
   * @return The badge
   */
  BadgeEntity retrieveBadge(RetrieveBadgeParams params);
}
