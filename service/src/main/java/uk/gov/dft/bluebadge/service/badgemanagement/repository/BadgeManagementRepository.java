package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.ConvertUtils;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.mapper.BadgeManagementMapper;

/** Provides CRUD operations on BadgeEntity entity. */
@Component
@Slf4j
public class BadgeManagementRepository implements BadgeManagementMapper {

  private final SqlSession sqlSession;

  public BadgeManagementRepository(SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  @Override
  public void createBadge(BadgeEntity entity) {
    log.debug("Persisting new badge {}", entity.getBadgeNo());
    sqlSession.insert("createBadge", entity);
  }

  @Override
  public Integer retrieveNextBadgeNumber() {
    return sqlSession.selectOne("retrieveNextBadgeNumber");
  }

  @Override
  public List<BadgeEntity> findBadges(FindBadgeParams params) {
    if (null != params.getName()) {
      params.setName(ConvertUtils.convertToUpperFullTextSearchParam(params.getName()));
    }
    if (null != params.getPostcode()) {
      params.setPostcode(ConvertUtils.formatPostcodeForEntity(params.getPostcode()));
    }
    return sqlSession.selectList("findBadges", params);
  }
}
