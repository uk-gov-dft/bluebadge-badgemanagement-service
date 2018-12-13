package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.badgemanagement.ApplicationContextTests;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchEntity;

@RunWith(SpringRunner.class)
@Transactional
public class BatchRepositoryIntTest extends ApplicationContextTests {

  @Autowired BatchRepository repository;

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void createBatch_ok() {
    BatchEntity batchEntity = repository.createBatch("STANDARD", "DFT", "PRINT");
    assertThat(batchEntity).isNotNull();
    assertThat(batchEntity.getId()).isNotNull();
    assertThat(batchEntity.getFilename()).startsWith("BADGEEXTRACT_");
    assertThat(batchEntity.getSource()).isEqualTo("DFT");
    assertThat(batchEntity.getPurpose()).isEqualTo("PRINT");
    assertThat(batchEntity.getCreated()).isNotNull();
  }

  @Test
  @Sql(scripts = "classpath:/test-data.sql")
  public void appendBadgesToBatch_ok() {
    BatchEntity batchEntity = repository.createBatch("FASTRACK", "DFT", "PRINT");
    repository.appendBadgesToBatch(batchEntity.getId(), "FASTRACK");
  }
}
