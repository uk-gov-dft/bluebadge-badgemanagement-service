package uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;

import javax.validation.Valid;
import java.util.List;

@Validated
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class ProcessedBatchesResponse extends CommonResponse {

  @Valid private List<ProcessedBatch> data;
}
