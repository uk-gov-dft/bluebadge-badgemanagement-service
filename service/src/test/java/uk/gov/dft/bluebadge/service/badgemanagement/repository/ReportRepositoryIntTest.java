package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.badgemanagement.ApplicationContextTests;
import uk.gov.dft.bluebadge.service.badgemanagement.model.IssuedBadge;
import uk.gov.dft.bluebadge.service.badgemanagement.model.ReportSearch;

@RunWith(SpringRunner.class)
@Transactional
@Sql(scripts = "classpath:/reports-test-data.sql")
public class ReportRepositoryIntTest extends ApplicationContextTests {

  @Autowired private ReportRepository repository;

  @Test
  public void findIssuedBadges() {
    ReportSearch search =
        ReportSearch.builder()
            .startDate(LocalDate.parse("2019-02-01"))
            .endDate(LocalDate.parse("2019-02-05"))
            .build();
    List<IssuedBadge> issuedBadges = repository.findIssuedBadges(search);

    assertThat(issuedBadges)
        .extracting("laShortCode", "badgeNumber")
        .containsExactly(
            tuple("TEST_1", "KAKKKC"),
            tuple("TEST_1", "KAKKKK"),
            tuple("TEST_2", "KAKKKD"),
            tuple("TEST_2", "KAKKKB"));
    assertThat(issuedBadges).extracting("issuedTimestamp").isNotNull();
  }

  @Test
  public void findIssuedBadges_empty() {
    ReportSearch search =
        ReportSearch.builder()
            .startDate(LocalDate.parse("2000-02-01"))
            .endDate(LocalDate.parse("2001-02-05"))
            .build();
    List<IssuedBadge> issuedBadges = repository.findIssuedBadges(search);

    assertThat(issuedBadges).isEmpty();
  }
}
