package uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.ReferenceDataApiClient;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model.ReferenceData;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ReferenceDataService {

  private static final Set<String> validGroupKeys = new HashSet<>();
  private static final Map<String, String> keyDescriptionMap = new HashMap<>();
  private ReferenceDataApiClient referenceDataApiClient;

  @Autowired
  public ReferenceDataService(ReferenceDataApiClient referenceDataApiClient) {
    this.referenceDataApiClient = referenceDataApiClient;
  }

  @SuppressWarnings("unused")
  @PostConstruct
  public void init() {

    List<ReferenceData> referenceDataList = referenceDataApiClient.retrieveReferenceData();
    for(ReferenceData item : referenceDataList){
      String key = item.getGroupShortCode() + "_" + item.getShortCode();
      validGroupKeys.add(key);
      keyDescriptionMap.put(key, item.getDescription());
    }
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
