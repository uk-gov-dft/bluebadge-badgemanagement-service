package uk.gov.dft.bluebadge.service.badgemanagement.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dft.bluebadge.service.badgemanagement.model.IssuedBadge;
import uk.gov.dft.bluebadge.service.badgemanagement.model.IssuedBadgesReportResponse;
import uk.gov.dft.bluebadge.service.badgemanagement.model.ReportSearch;
import uk.gov.dft.bluebadge.service.badgemanagement.service.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {
  private final ReportService reportService;

  public ReportController(ReportService reportService) {
    this.reportService = reportService;
  }

  @GetMapping("/issued-badges")
  public IssuedBadgesReportResponse issuedBadgesReport(@Valid ReportSearch reportSearch) {
    List<IssuedBadge> report = reportService.issuedBadgesReport(reportSearch);
    IssuedBadgesReportResponse result = new IssuedBadgesReportResponse();
    result.setData(report);
    return result;
  }
}
