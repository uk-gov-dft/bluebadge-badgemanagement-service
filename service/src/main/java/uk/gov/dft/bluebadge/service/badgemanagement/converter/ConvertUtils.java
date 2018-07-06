package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import org.apache.commons.lang3.StringUtils;

public class ConvertUtils {

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
}
