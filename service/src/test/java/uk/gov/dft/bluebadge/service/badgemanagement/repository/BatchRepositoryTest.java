package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.BatchRepository.Statements.CREATE;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchEntity;

public class BatchRepositoryTest {

  @Mock SqlSession sqlSession;

  BatchRepository repository;

  public BatchRepositoryTest() {
    MockitoAnnotations.initMocks(this);
    repository = new BatchRepository(sqlSession);
  }

  @Test
  public void createBatch() {
    //BatchEntity batchEntity  = BatchEntity.builder().build();
    //List<BadgeEntity> badges = Lists.newArrayList(BadgeEntity.builder().build());
    BatchEntity batchEntity = repository.createBatch("STANDARD", "DFT", "PRINT");
    assertThat(batchEntity).isNotNull();
    verify(sqlSession).insert(eq(CREATE), any(BatchEntity.class));
  }
}
