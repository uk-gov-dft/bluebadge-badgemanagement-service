package uk.gov.dft.bluebadge.service.badgemanagement.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class RefData {

  private static final Set<String> validGroupKeys = new HashSet<>();
  private static final Map<String, String> keyDescriptionMap = new HashMap<>();

  @SuppressWarnings("unused")
  @PostConstruct
  public void init() {
    // TODO load ref data from service.
    validGroupKeys.addAll(
        Arrays.asList(
            "APPSOURCE_PAPER",
            "APPSOURCE_PHONE",
            "APPSOURCE_INPERSON",
            "APPSOURCE_ONLINE",
            "DELIVER_HOME",
            "DELIVER_COUNCIL",
            "DELOP_STAND",
            "DELOP_FAST",
            "ELIGIBILIT_PIP",
            "ELIGIBILIT_DLA",
            "ELIGIBILIT_AFRFCS",
            "ELIGIBILIT_WPMS",
            "ELIGIBILIT_BLIND",
            "ELIGIBILIT_WALKD",
            "ELIGIBILIT_ARMS",
            "ELIGIBILIT_CHILDBULK",
            "ELIGIBILIT_CHILDVEHIC",
            "ELIGIBILIT_TERMILL",
            "GENDER_FEMALE",
            "GENDER_MALE",
            "GENDER_UNSPECIFIE",
            "PARTY_PERSON",
            "PARTY_ORG",
            "STATUS_AWAITCOLL",
            "STATUS_AWAITING",
            "STATUS_CANCELLED",
            "STATUS_EXTRACTED",
            "STATUS_FAILED",
            "STATUS_ISSUED",
            "STATUS_NEW",
            "STATUS_PENDCANC",
            "STATUS_REFUSED",
            "STATUS_REISSUED",
            "STATUS_REPLACED",
            "STATUS_RETCAN",
            "STATUS_RETURNED",
            "STATUS_SUBMITTED"));

    // Enough values for dev of findBadges.
    keyDescriptionMap.put("PARTY_PERSON", "Person");
    keyDescriptionMap.put("PARTY_ORG", "Organisation");
    keyDescriptionMap.put("STATUS_NEW", "New");
  }

  public static boolean groupContainsKey(RefDataGroupEnum group, String code) {
    String key = group.getGroupKey() + "_" + code;
    return validGroupKeys.contains(key);
  }

  public static String getDescription(RefDataGroupEnum group, String code) {
    Assert.notNull(group, "Must provide group code to retrieve ref data.");

    return null == code ? null : keyDescriptionMap.get(group.getGroupKey() + "_" + code);
  }

  public static Map<String, String> getKeyDescriptionMap() {
    return keyDescriptionMap;
  }

  public static Set<String> getValidGroupKeys() {
    return validGroupKeys;
  }
}
