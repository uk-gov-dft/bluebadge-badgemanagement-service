package uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Validated
@Data
@Builder
public class ProcessedBadge {

  public enum CancellationEnum {
    NO,
    YES;

    public static CancellationEnum fromValue(String value) {
      if ("no".equals(value)) return NO;
      if ("yes".equals(value)) return YES;
      return null;
    }
  }

  private CancellationEnum cancellation;
  private OffsetDateTime dispatchedDate;
  @NotNull private String badgeNumber;
  private String errorMessage;
}
