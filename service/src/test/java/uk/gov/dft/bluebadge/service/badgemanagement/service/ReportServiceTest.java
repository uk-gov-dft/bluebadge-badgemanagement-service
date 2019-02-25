package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.service.badgemanagement.model.ReportSearch;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.ReportRepository;

public class ReportServiceTest {
  private ReportService reportService;
  @Mock ReportRepository mockReportRepository;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    reportService = new ReportService(mockReportRepository);
  }

  @Test
  public void issuedBadgesReport_datesAdjustedForQuery() {
    ReportSearch reportSearch =
        ReportSearch.builder()
            .startDate(LocalDate.parse("2019-12-03"))
            .endDate(LocalDate.parse("2019-12-05"))
            .build();
    reportService.issuedBadgesReport(reportSearch);

    ArgumentCaptor<ReportSearch> captor = ArgumentCaptor.forClass(ReportSearch.class);
    verify(mockReportRepository).findIssuedBadges(captor.capture());
    assertThat(captor.getValue()).isNotNull();
    assertThat(captor.getValue().getStartDate()).isEqualTo("2019-12-03");
    assertThat(captor.getValue().getEndDate()).isEqualTo("2019-12-06");
  }
}
