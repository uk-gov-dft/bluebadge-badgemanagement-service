package uk.gov.dft.bluebadge.service.badgemanagement.model;

import java.time.LocalDate;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
public class ReportSearch {
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @NotNull
  private LocalDate startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @NotNull
  private LocalDate endDate;

  @AssertTrue(message = "ReportSearch.startDateAfterEndDate")
  public boolean isStartDateAfterEndDate() {
    if (null != startDate && null != endDate) {
      return !startDate.isAfter(endDate);
    }
    return true;
  }
}
