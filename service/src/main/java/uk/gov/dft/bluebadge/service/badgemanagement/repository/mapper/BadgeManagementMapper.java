package uk.gov.dft.bluebadge.service.badgemanagement.repository.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.*;

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
   * Retrieve list of badges for print batch.
   *
   * @param params holds batch type
   * @return List of BadgeEntity
   */
  List<BadgeEntity> findBadgesForPrintBatch(FindBadgesForPrintBatchParams params);

  /**
   * Updates statuses of badges belonging to a batch
   *
   * @param params holds batch id and status to be used to set the badges
   */
  void updateBadgesStatusesForBatch(UpdateBadgesStatusesForBatchParams params);

  /**
   * Retrieve a single badge
   *
   * @param params Holds badge number to retrieve
   * @return The badge
   */
  BadgeEntity retrieveBadge(RetrieveBadgeParams params);

  /**
   * Cancel a badge.
   *
   * @param params Contains badge no and cancel reason.
   * @return Number of updates (0 or 1)
   */
  int cancelBadge(CancelBadgeParams params);

  int deleteBadge(DeleteBadgeParams badgeEntity);

  int replaceBadge(ReplaceBadgeParams params);

  int updateBadgeStatusFromStatus(UpdateBadgeStatusParams params);

  List<String> findBadgeHash(byte[] hash);
}
