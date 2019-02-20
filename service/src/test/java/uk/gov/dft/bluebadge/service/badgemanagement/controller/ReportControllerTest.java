package uk.gov.dft.bluebadge.service.badgemanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.dft.bluebadge.service.badgemanagement.model.ReportSearch;
import uk.gov.dft.bluebadge.service.badgemanagement.service.ReportService;

public class ReportControllerTest {
  private MockMvc mockMvc;
  @Mock private ReportService mockReportService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    ReportController controller = new ReportController(mockReportService);

    this.mockMvc =
        MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new CommonResponseControllerAdvice())
            .build();
  }

  @Test
  @SneakyThrows
  public void issuedBadges() {
    mockMvc
        .perform(
            get("/reports/issued-badges")
                .param("startDate", "2019-02-02")
                .param("endDate", "2019-03-02"))
        .andExpect(status().isOk());

    ArgumentCaptor<ReportSearch> captor = ArgumentCaptor.forClass(ReportSearch.class);
    verify(mockReportService).issuedBadgesReport(captor.capture());
    assertThat(captor.getValue()).isNotNull();
    assertThat(captor.getValue().getStartDate()).isEqualTo("2019-02-02");
    assertThat(captor.getValue().getEndDate()).isEqualTo("2019-03-02");
  }

  @Test
  @SneakyThrows
  public void issuedBadges_nullStartDate() {
    mockMvc
        .perform(get("/reports/issued-badges").param("endDate", "2019-03-02"))
        .andExpect(status().isBadRequest())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.apiVersion").hasJsonPath())
        .andExpect(jsonPath("$.error.errors", hasSize(1)))
        .andExpect(jsonPath("$.error.errors[0].field", equalTo("startDate")));

    verifyZeroInteractions(mockReportService);
  }

  @Test
  @SneakyThrows
  public void issuedBadges_nullEndDate() {
    mockMvc
        .perform(get("/reports/issued-badges").param("startDate", "2019-02-02"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error.errors", hasSize(1)))
        .andExpect(jsonPath("$.error.errors[0].field", equalTo("endDate")));

    verifyZeroInteractions(mockReportService);
  }

  @Test
  @SneakyThrows
  public void issuedBadges_endDateBeforeStart() {
    mockMvc
        .perform(
            get("/reports/issued-badges")
                .param("endDate", "2019-02-02")
                .param("startDate", "2019-03-02"))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.error.errors[0].reason", equalTo("ReportSearch.startDateAfterEndDate")));

    verifyZeroInteractions(mockReportService);
  }
}
