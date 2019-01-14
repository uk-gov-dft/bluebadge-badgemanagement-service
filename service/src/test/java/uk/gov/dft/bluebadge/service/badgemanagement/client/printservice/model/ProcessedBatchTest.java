package uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ProcessedBatchTest {

  @Test
  public void getBatchName_test() {
    assertThat(ProcessedBatch.builder().filename("ihaveanextension.txt").build().getBatchName())
        .isEqualTo("ihaveanextension");
    assertThat(ProcessedBatch.builder().filename("idont").build().getBatchName())
        .isEqualTo("idont");
  }
}
