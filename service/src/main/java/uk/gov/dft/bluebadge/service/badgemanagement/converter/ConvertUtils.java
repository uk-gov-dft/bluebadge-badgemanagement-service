package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import org.apache.commons.lang3.StringUtils;

import java.time.OffsetDateTime;
import java.time.ZoneId;

public class ConvertUtils {

  private ConvertUtils() {}

  public static String formatBadgeNoForQuery(String badgeNumber) {
    if (null == badgeNumber) return null;

    return StringUtils.stripToNull(badgeNumber.toUpperCase());
  }

  public static String formatPostcodeForEntity(String postcode) {
    if (null == postcode) return null;

    return StringUtils.removeAll(postcode.toUpperCase(), " ");
  }

  public static String convertToUpperFullTextSearchParam(String param) {
    if (null == param) return null;

    if (!param.startsWith("%")) {
      param = "%" + param;
    }

    if (!param.endsWith("%")) {
      param = param + "%";
    }
    return param.toUpperCase();
  }

  public static OffsetDateTime getAsUTC(OffsetDateTime time){
    return OffsetDateTime.ofInstant(time.toInstant(), ZoneId.of("UTC"));
  }
}
