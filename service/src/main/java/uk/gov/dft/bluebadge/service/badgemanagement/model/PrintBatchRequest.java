package uk.gov.dft.bluebadge.service.badgemanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/** PrintBatchRequest * */
@Validated
public class PrintBatchRequest {
  @JsonProperty("batchType")
  private BatchType batchType = null;

  public PrintBatchRequest batchType(BatchType batchType) {
    this.batchType = batchType;
    return this;
  }

  /**
   * The unique batch type for this print batch request.
   *
   * @return batch type
   */
  @ApiModelProperty(
    example = "FASTTRACK",
    value = "'Batch type. Could be FASTTRACK or STANDARD or LA'"
  )
  @NotNull
  public BatchType getBatchType() {
    return batchType;
  }

  public void setBatchType(BatchType batchType) {
    this.batchType = batchType;
  }
}
