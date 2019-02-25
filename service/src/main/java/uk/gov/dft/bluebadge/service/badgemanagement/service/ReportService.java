package uk.gov.dft.bluebadge.service.badgemanagement.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.service.badgemanagement.model.IssuedBadge;
import uk.gov.dft.bluebadge.service.badgemanagement.model.ReportSearch;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.ReportRepository;

@Service
public class ReportService {
  private final ReportRepository reportRepository;

  public ReportService(ReportRepository reportRepository) {
    this.reportRepository = reportRepository;
  }

  public List<IssuedBadge> issuedBadgesReport(ReportSearch reportSearch) {
    Assert.notNull(reportSearch, "reportSearch cannot be null.");

    LocalDate tmp = reportSearch.getEndDate();
    reportSearch.setEndDate(tmp.plusDays(1));

    return reportRepository.findIssuedBadges(reportSearch);
  }
}
