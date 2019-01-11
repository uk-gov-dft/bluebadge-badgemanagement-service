package uk.gov.dft.bluebadge.service.badgemanagement.repository.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import uk.gov.dft.bluebadge.service.badgemanagement.model.BatchType;

import javax.validation.constraints.NotNull;

/** Bean to hold a BatchEntity record. */
@Alias("BatchEntity")
@Data
@Builder
public class BatchEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  public enum SourceEnum {
    DFT, PRINTER
  }

  public enum PurposeEnum{
    ISSUED, REJECTED, LA, STANDARD, FASTTRACK;

    public static PurposeEnum fromBatchType(BatchType batchType){
      switch (batchType){
        case LA:
          return LA;
        case STANDARD:
          return STANDARD;
        case FASTTRACK:
          return FASTTRACK;
      }
      return null;
    }
  }
  Integer id;
  @NotNull
  String filename;
  LocalDateTime created;
  @NotNull
  SourceEnum source;
  @NotNull
  PurposeEnum purpose;
}
