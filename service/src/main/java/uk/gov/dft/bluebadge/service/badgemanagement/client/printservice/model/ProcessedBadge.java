package uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model;

import java.time.OffsetDateTime;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@Builder
public class ProcessedBadge {

  public enum CancellationEnum {
    NO,
    YES;

    public static CancellationEnum fromValue(String value) {
      if ("no".equalsIgnoreCase(value)) return NO;
      if ("yes".equalsIgnoreCase(value)) return YES;
      return null;
    }
  }

  private CancellationEnum cancellation;
  private OffsetDateTime dispatchedDate;
  @NotNull private String badgeNumber;
  private String errorMessage;
}
