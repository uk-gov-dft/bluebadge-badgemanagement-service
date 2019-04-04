package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.service.badgemanagement.model.BatchType;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.AppendBadgesToBatchParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchBadgeLinkEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.mapper.BatchMapper;

/** Provides CRUD operations on BatchEntity entity. */
@Component
@Slf4j
public class BatchRepository implements BatchMapper {

  class Statements {

    private Statements() {}

    static final String CREATE = "createBatch";
    static final String APPEND_BADGES = "appendBadges";
    static final String LINK_BADGE_TO_BATCH = "linkBadgeToBatch";
    static final String RETRIEVE_BATCH = "retrieveBatch";
  }

  private final SqlSession sqlSession;

  public BatchRepository(SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  @Override
  public BatchEntity createBatch(
      BatchEntity.SourceEnum source, BatchEntity.PurposeEnum purpose, String filename) {
    log.debug("Create batch");

    LocalDateTime localDateTime = LocalDateTime.now();

    BatchEntity batch =
        BatchEntity.builder()
            .created(localDateTime)
            .filename(filename)
            .purpose(purpose)
            .source(source)
            .build();
    sqlSession.insert(Statements.CREATE, batch);
    return batch;
  }

  @Override
  public void appendBadgesToBatch(Integer batchId, BatchType batchType) {
    AppendBadgesToBatchParams params =
        AppendBadgesToBatchParams.builder().batchId(batchId).batchType(batchType.name()).build();
    sqlSession.insert(Statements.APPEND_BADGES, params);
  }

  @Override
  public int linkBadgeToBatch(BatchBadgeLinkEntity params) {
    return sqlSession.insert(Statements.LINK_BADGE_TO_BATCH, params);
  }

  public BatchEntity retrieveBatchEntity(Integer batchId) {
    Assert.notNull(batchId, "batchId must not be null");
    return sqlSession.selectOne(Statements.RETRIEVE_BATCH, batchId);
  }
}
