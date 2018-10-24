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
  public void test_loadCombinations() {
    assertThat(filter.getCombinations().size(), is(18));
  }

  @Test
  public void test_invalidBadge() {
    filter
        .getCombinations()
        .stream()
        .forEach(
            i -> {
              String badge = randomBadge() + i.toUpperCase();
              assertFalse(filter.valid(badge));
            });
  }

  @Test
  public void test_validBadge() {
    String badge = "23KH5";
    assertTrue(filter.valid(badge));
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
