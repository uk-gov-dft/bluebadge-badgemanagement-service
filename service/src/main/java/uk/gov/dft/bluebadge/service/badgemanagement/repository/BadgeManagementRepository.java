package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.ConvertUtils;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.RetrieveBadgeParams;

/** Provides CRUD operations on BadgeEntity entity. */
@Component
@Slf4j
public class BadgeManagementRepository {

  class Statements {

    private Statements() {}

    static final String CREATE = "createBadge";
    static final String FIND = "findBadges";
    static final String RETRIEVE = "retrieveBadge";
    static final String CANCEL = "cancelBadge";
  }

  private final SqlSession sqlSession;

  public BadgeManagementRepository(SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  //  @Override
  public void createBadge(BadgeEntity entity) {
    log.debug("Persisting new badge {}", entity.getBadgeNo());
    sqlSession.insert(Statements.CREATE, entity);
  }

  //  @Override
  public Integer retrieveNextBadgeNumber() {
    return sqlSession.selectOne("retrieveNextBadgeNumber");
  }

  //  @Override
  public List<BadgeEntity> findBadges(FindBadgeParams params) {
    Assert.notNull(params, "params cannot be null.");
    if (null != params.getName()) {
      params.setName(ConvertUtils.convertToUpperFullTextSearchParam(params.getName()));
    }
    if (null != params.getPostcode()) {
      params.setPostcode(ConvertUtils.formatPostcodeForEntity(params.getPostcode()));
    }
    return sqlSession.selectList(Statements.FIND, params);
  }

  //  @Override
  public BadgeEntity retrieveBadge(RetrieveBadgeParams params) {
    params.setBadgeNo(ConvertUtils.formatBadgeNoForQuery(params.getBadgeNo()));
    Assert.notNull(params.getBadgeNo(), "Cannot retrieve with null badge number");
    return sqlSession.selectOne(Statements.RETRIEVE, params);
  }

  //  @Override
  public int cancelBadge(CancelBadgeParams params) {
    return sqlSession.update(Statements.CANCEL, params);
  }
}
