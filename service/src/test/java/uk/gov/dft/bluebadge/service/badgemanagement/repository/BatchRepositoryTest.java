package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.BatchRepository.Statements.APPEND_BADGES;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.BatchRepository.Statements.CREATE;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.service.badgemanagement.model.BatchType;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.AppendBadgesToBatchParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchEntity;

public class BatchRepositoryTest {

  @Mock SqlSession sqlSession;

  private BatchRepository repository;

  public BatchRepositoryTest() {
    MockitoAnnotations.initMocks(this);
    repository = new BatchRepository(sqlSession);
  }

  @Test
  public void createBatch() {
    BatchEntity batchEntity =
        repository.createBatch(
            BatchEntity.SourceEnum.DFT, BatchEntity.PurposeEnum.STANDARD, "AFile.txt");
    assertThat(batchEntity).isNotNull();
    verify(sqlSession).insert(eq(CREATE), any(BatchEntity.class));
  }

  @Test
  public void appendBadgesToBatch() {
    repository.appendBadgesToBatch(1, BatchType.STANDARD);
    verify(sqlSession).insert(eq(APPEND_BADGES), any(AppendBadgesToBatchParams.class));
  }
}
