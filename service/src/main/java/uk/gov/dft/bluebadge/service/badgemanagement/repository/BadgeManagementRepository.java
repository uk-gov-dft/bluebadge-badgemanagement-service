package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.ConvertUtils;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.*;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.mapper.BadgeManagementMapper;

/** Provides CRUD operations on BadgeEntity entity. */
@Component
@Slf4j
public class BadgeManagementRepository implements BadgeManagementMapper {

  class Statements {

    private Statements() {}

    static final String CREATE = "createBadge";
    static final String FIND = "findBadges";
    static final String RETRIEVE = "retrieveBadge";
    static final String CANCEL = "cancelBadge";
    static final String DELETE = "deleteBadge";
    static final String REPLACE = "replaceBadge";
    static final String FIND_BADGES_FOR_PRINT_BATCH = "findBadgesForPrintBatch";
    static final String UPDATE_BADGES_STATUSES_FOR_PRINT_BATCH =
        "updateBadgesStatusesForPrintBatch";
    static final String UPDATE_BADGE_STATUS_FROM_STATUS = "updateBadgeStatusFromStatus";
    static final String FIND_BADGES_WITH_HASH = "findBadgeHash";
    static final String RETRIEVE_BADGES_BY_LA = "retrieveBadgesByLa";
  }

  private final SqlSession sqlSession;

  BadgeManagementRepository(SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  @Override
  public void createBadge(BadgeEntity entity) {
    log.debug("Persisting new badge {}", entity.getBadgeNo());
    sqlSession.insert(Statements.CREATE, entity);
  }

  @Override
  public Integer retrieveNextBadgeNumber() {
    return sqlSession.selectOne("retrieveNextBadgeNumber");
  }

  //  @Override
  /*  public List<BadgeEntity> findBadges(FindBadgeParams params) {
    return findBadges(params, DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
  }*/

  @Override
  public Page<BadgeEntity> findBadges(FindBadgeParams params, Integer pageNum, Integer pageSize) {
    Assert.notNull(params, "params cannot be null.");
    Assert.notNull(pageNum, "pageNum is null");
    Assert.notNull(pageSize, "pageSize is null");

    if (null != params.getName()) {
      params.setName(ConvertUtils.convertToUpperFullTextSearchParam(params.getName()));
    }
    if (null != params.getPostcode()) {
      params.setPostcode(ConvertUtils.formatPostcodeForEntity(params.getPostcode()));
    }

    return PageHelper.startPage(pageNum, pageSize, true)
        .doSelectPage(() -> sqlSession.selectList(Statements.FIND, params));
  }

  @Override
  public List<BadgeEntity> findBadgesForPrintBatch(FindBadgesForPrintBatchParams params) {
    Assert.notNull(params, "params cannot be null");
    return sqlSession.selectList(Statements.FIND_BADGES_FOR_PRINT_BATCH, params);
  }

  @Override
  public void updateBadgesStatusesForBatch(UpdateBadgesStatusesForBatchParams params) {
    Assert.notNull(params, "params cannot be null");
    sqlSession.update(Statements.UPDATE_BADGES_STATUSES_FOR_PRINT_BATCH, params);
  }

  @Override
  public BadgeEntity retrieveBadge(RetrieveBadgeParams params) {
    params.setBadgeNo(ConvertUtils.formatBadgeNoForQuery(params.getBadgeNo()));
    Assert.notNull(params.getBadgeNo(), "Cannot retrieve with null badge number");
    return sqlSession.selectOne(Statements.RETRIEVE, params);
  }

  @Override
  public int cancelBadge(CancelBadgeParams params) {
    return sqlSession.update(Statements.CANCEL, params);
  }

  @Override
  public int deleteBadge(DeleteBadgeParams deleteBadgeParams) {
    return sqlSession.update(Statements.DELETE, deleteBadgeParams);
  }

  @Override
  public int replaceBadge(ReplaceBadgeParams params) {
    return sqlSession.update(Statements.REPLACE, params);
  }

  /**
   * Update badge status. Will only update if the badge is currently set to the fromStatus. Returns
   * 1 if update done, 0 else.
   *
   * @param params fromStatus, toStatus.
   * @return Update count.
   */
  @Override
  public int updateBadgeStatusFromStatus(UpdateBadgeStatusParams params) {
    return sqlSession.update(Statements.UPDATE_BADGE_STATUS_FROM_STATUS, params);
  }

  @Override
  public List<String> findBadgeHash(byte[] hash) {
    return sqlSession.selectList(
        Statements.FIND_BADGES_WITH_HASH, FindBadgeHashParams.builder().hash(hash).build());
  }

  @Override
  public List<BadgeZipEntity> retrieveBadgesByLa(String laShortCode) {
    return sqlSession.selectList(
        Statements.RETRIEVE_BADGES_BY_LA, new RetrieveBadgesByLaParams(laShortCode));
  }
}
