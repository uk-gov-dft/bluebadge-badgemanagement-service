package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.badgemanagement.ApplicationContextTests;
import uk.gov.dft.bluebadge.service.badgemanagement.model.BatchType;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.LinkBadgeToBatchParams;

@RunWith(SpringRunner.class)
@Transactional
public class BatchRepositoryIntTest extends ApplicationContextTests {

  @Autowired BatchRepository repository;

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void createBatch_ok() {
    BatchEntity batchEntity =
        repository.createBatch(BatchEntity.SourceEnum.DFT, BatchEntity.PurposeEnum.STANDARD);
    assertThat(batchEntity).isNotNull();
    assertThat(batchEntity.getId()).isNotNull();
    assertThat(batchEntity.getFilename()).startsWith("BADGEEXTRACT_");
    assertThat(batchEntity.getSource()).isEqualTo(BatchEntity.SourceEnum.DFT);
    assertThat(batchEntity.getPurpose()).isEqualTo(BatchEntity.PurposeEnum.STANDARD);
    assertThat(batchEntity.getCreated()).isNotNull();
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void appendBadgesToBatch_ok() {
    BatchEntity batchEntity =
        repository.createBatch(BatchEntity.SourceEnum.DFT, BatchEntity.PurposeEnum.FASTTRACK);
    repository.appendBadgesToBatch(batchEntity.getId(), BatchType.FASTTRACK);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void linkBadgeToBatch() {
    // When badge and batch exist, then link created
    assertThat(
            repository.linkBadgeToBatch(
                LinkBadgeToBatchParams.builder().badgeId("KKKKDC").batchId(-2).build()))
        .isEqualTo(1);
  }

  @Test
  public void linkBadgeToBatch_invalid() {

    try {
      repository.linkBadgeToBatch(
          LinkBadgeToBatchParams.builder().batchId(-20000).badgeId("ZZZZZZ").build());
    } catch (DataIntegrityViolationException e) {
      return;
    }
    // Should get exception thrown.
    assertThat(true).isFalse();
  }
}
