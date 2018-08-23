package uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.ReferenceDataApiClient;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model.ReferenceData;

@Component
public class ReferenceDataService {

  private final Set<String> validGroupKeys = new HashSet<>();
  private final Map<String, String> keyDescriptionMap = new HashMap<>();
  private final ReferenceDataApiClient referenceDataApiClient;
  private boolean isLoaded = false;

  @Autowired
  public ReferenceDataService(ReferenceDataApiClient referenceDataApiClient) {
    this.referenceDataApiClient = referenceDataApiClient;
  }

  /**
   * Load the ref data first time required. Chose not to do PostConstruct so that can start service
   * if ref data service is still starting.
   */
  private void init() {
    if (!isLoaded) {
      List<ReferenceData> referenceDataList = referenceDataApiClient.retrieveReferenceData();
      for (ReferenceData item : referenceDataList) {
        String key = item.getGroupShortCode() + "_" + item.getShortCode();
        validGroupKeys.add(key);
        keyDescriptionMap.put(key, item.getDescription());
      }
      isLoaded = true;
    }
  }

  /**
   * Check a reference data group contains the key provided.
   *
   * @param group The group to check.
   * @param code The expected value.
   * @return true if present.
   */
  public boolean groupContainsKey(RefDataGroupEnum group, String code) {
    if (!isLoaded) init();

    String key = group.getGroupKey() + "_" + code;
    return validGroupKeys.contains(key);
  }
}
