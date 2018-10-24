package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlacklistedCombinationsFilterTest {

  @Autowired private BlacklistedCombinationsFilter filter;

  @Test
  public void shouldLoadCombinations() {
    assertThat(filter.getCombinations().size(), is(18));
  }

  @Test
  public void shouldFailBadgeWithInvalidCombinations() {
    filter
        .getCombinations()
        .stream()
        .forEach(
            i -> {
              String badge = randomBadge() + i.toUpperCase();
              assertFalse(filter.isValid(badge));
            });
  }

  @Test
  public void shouldPassValidBadge() {
    String badge = "23KH5";
    assertTrue(filter.isValid(badge));
  }

  private String randomBadge() {
    String choices = "ABCDEFHJKLMN23456789";
    StringBuilder salt = new StringBuilder();
    Random rnd = new Random();
    while (salt.length() < 5) {
      int index = (int) (rnd.nextFloat() * choices.length());
      salt.append(choices.charAt(index));
    }
    return salt.toString();
  }
}
