package uk.gov.dft.bluebadge.service.badgemanagement.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class RefData {

  private static final Set<String> validGroupKeys = new HashSet<>();

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
  }

  public static boolean groupContainsKey(RefDataGroupEnum group, String code) {
    String key = group.getGroupKey() + "_" + code;
    return validGroupKeys.contains(key);
  }

  public static Set<String> getValidGroupKeys() {
    return validGroupKeys;
  }
}
