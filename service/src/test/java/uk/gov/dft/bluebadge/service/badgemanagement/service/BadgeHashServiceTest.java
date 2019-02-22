package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import javax.xml.bind.DatatypeConverter;
import org.junit.Test;

public class BadgeHashServiceTest {

  @Test
  public void toSha256_Test() {
    // Compare to a value created via postgres digest function to ensure update script will work.
    assertThat(BadgeHashService.toSha256("kkk"))
        .isEqualTo(
            DatatypeConverter.parseHexBinary(
                "96efbc43a462ab9d9c6a8173e5b322e17f218b56eb3a05a4bbc53221adebc7b3"));
  }

  @Test
  public void coalesceValueString_Test() {
    assertThat(BadgeHashService.coalesceValueString(null)).isEqualTo("X");
    assertThat(BadgeHashService.coalesceValueString("qwe")).isEqualTo("qwe");
  }

  @Test
  public void coalesceValueDate_Test() {
    assertThat(BadgeHashService.coalesceValueDate(null)).isEqualTo("1970-01-01");
    assertThat(BadgeHashService.coalesceValueDate(LocalDate.of(2012, 2, 28)))
        .isEqualTo("2012-02-28");
  }
}
