package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.service.badgemanagement.model.IssuedBadge;
import uk.gov.dft.bluebadge.service.badgemanagement.model.ReportSearch;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.mapper.ReportMapper;

/** Provides CRUD operations on BatchEntity entity. */
@Component
@Slf4j
public class ReportRepository implements ReportMapper {
  private final SqlSession sqlSession;

  public ReportRepository(SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  @Override
  public List<IssuedBadge> findIssuedBadges(ReportSearch reportSearch) {
    Assert.notNull(reportSearch, "reportSearch cannot be null.");
    return sqlSession.selectList("findIssuedBadges", reportSearch);
  }
}
