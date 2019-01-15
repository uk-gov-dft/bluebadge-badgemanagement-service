package uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@Builder
@EqualsAndHashCode
public class ProcessedBatch {

  public enum FileTypeEnum {
    CONFIRMATION,
    REJECTION
  }

  @NotEmpty private String filename;
  private String errorMessage;
  @NotNull private FileTypeEnum fileType;
  @NotNull private List<ProcessedBadge> processedBadges;
}
