package uk.gov.dft.bluebadge.service.badgemanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

/** PrintBatchRequest * */
@Validated
public class PrintBatchRequest {
  @JsonProperty("batchType")
  private String batchType = null;

  public PrintBatchRequest batchType(String batchType) {
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
  @Pattern(regexp = "^STANDARD|FASTTRACK|LA+$")
  public String getBatchType() {
    return batchType;
  }

  public void setBatchType(String batchType) {
    this.batchType = batchType;
  }
}
