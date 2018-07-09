package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class ConvertUtilsTest {

  @Test
  public void formatPostcodeForEntity() {
    // Given postcode has lowercase chars and a space
    String postcode = "wv16 4aw";
    // When converted
    postcode = ConvertUtils.formatPostcodeForEntity(postcode);
    // Then space removed and uppercase.
    Assert.assertEquals("WV164AW", postcode);
  }

  @Test
  public void formatPostcodeForEntity_null_safe() {
    // Given postcode null
    // When converted all ok
    ConvertUtils.formatPostcodeForEntity(null);
  }

  @Test
  public void convertToUpperFullTextSearchParam() {
    String searchParamLeadingDone = "%param";
    String searchParamTrailingDone = "param%";
    String searchParamNeither = "param";

    // Given lower case and already has % at start
    // When converted
    String result = ConvertUtils.convertToUpperFullTextSearchParam(searchParamLeadingDone);
    // Then has a single % at each end and uppercase
    Assert.assertEquals("%PARAM%", result);

    // Given lower case and already has % at end
    // When converted
    result = ConvertUtils.convertToUpperFullTextSearchParam(searchParamTrailingDone);
    // Then has a single % at each end and uppercase
    Assert.assertEquals("%PARAM%", result);

    // Given lower case
    // When converted
    result = ConvertUtils.convertToUpperFullTextSearchParam(searchParamNeither);
    // Then has a single % at each end and uppercase
    Assert.assertEquals("%PARAM%", result);
  }

  @Test
  public void convertToUpperFullTextSearchParam_null_safe() {
    ConvertUtils.convertToUpperFullTextSearchParam(null);
  }

  @Test
  public void formatBadgeNoForQuery(){
    String result = ConvertUtils.formatBadgeNoForQuery("a123cc");
    Assert.assertEquals("A123CC", result);

    result = ConvertUtils.formatBadgeNoForQuery(" a ");
    Assert.assertEquals("A", result);

    result = ConvertUtils.formatBadgeNoForQuery("  ");
    Assert.assertNull(result);
  }
}
