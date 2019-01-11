package uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Data
@Builder
@EqualsAndHashCode
public class ProcessedBatch {

  public enum FileTypeEnum {
    CONFIRMATION,
    REJECTION
  }

  @NotEmpty
  private String filename;
  private String errorMessage;
  @NotNull
  private FileTypeEnum fileType;
  @NotNull
  private List<ProcessedBadge> processedBadges;

  @JsonIgnore
  public String getBatchName(){
    int suffixLocation = filename.lastIndexOf(".");
    if(suffixLocation > 0){
      return filename.substring(0, suffixLocation);
    }
    return filename;
  }
}
