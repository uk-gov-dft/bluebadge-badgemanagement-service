package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.badgemanagement.ApplicationContextTests;
import uk.gov.dft.bluebadge.service.badgemanagement.model.BatchType;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchBadgeLinkEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchEntity;

@RunWith(SpringRunner.class)
@Transactional
public class BatchRepositoryIntTest extends ApplicationContextTests {

  @Autowired private BatchRepository repository;

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void createBatch_ok() {
    BatchEntity batchEntity =
        repository.createBatch(
            BatchEntity.SourceEnum.DFT, BatchEntity.PurposeEnum.STANDARD, "BADGEEXTRACT_123");
    assertThat(batchEntity).isNotNull();
    assertThat(batchEntity.getId()).isNotNull();
    assertThat(batchEntity.getFilename()).isEqualTo("BADGEEXTRACT_123");
    assertThat(batchEntity.getSource()).isEqualTo(BatchEntity.SourceEnum.DFT);
    assertThat(batchEntity.getPurpose()).isEqualTo(BatchEntity.PurposeEnum.STANDARD);
    assertThat(batchEntity.getCreated()).isNotNull();
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void appendBadgesToBatch_ok() {
    BatchEntity batchEntity =
        repository.createBatch(
            BatchEntity.SourceEnum.DFT, BatchEntity.PurposeEnum.FASTTRACK, "BADGEEXTRACT_123");
    repository.appendBadgesToBatch(batchEntity.getId(), BatchType.FASTTRACK);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void linkBadgeToBatch() {
    // When badge and batch exist, then link created
    assertThat(
            repository.linkBadgeToBatch(
                BatchBadgeLinkEntity.builder().badgeId("KKKKDC").batchId(-2).build()))
        .isEqualTo(1);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void retrieveBatch() {
    BatchEntity batchEntity = repository.retrieveBatchEntity(-1);
    assertThat(batchEntity.getId()).isEqualTo(-1);
    assertThat(batchEntity.getFilename()).isEqualTo("filename.txt");
    assertThat(batchEntity.getPurpose()).isEqualTo(BatchEntity.PurposeEnum.STANDARD);
    assertThat(batchEntity.getSource()).isEqualTo(BatchEntity.SourceEnum.DFT);
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void badgeIsIssued() {
    assertThat(repository.badgeAlreadyProcessed("NNNJMH")).isTrue();
    assertThat(repository.badgeAlreadyProcessed("ZXCVBN")).isFalse();
  }
}
