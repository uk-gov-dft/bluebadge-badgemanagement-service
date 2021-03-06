package uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model.ReferenceData;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model.ReferenceDataResponse;

@Slf4j
@Service
public class ReferenceDataApiClient {

  private final RestTemplate restTemplate;

  @Autowired
  ReferenceDataApiClient(@Qualifier("referenceDataServiceRestTemplate") RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * Retrieve badge reference data
   *
   * @return List of reference data items.
   */
  public List<ReferenceData> retrieveReferenceData() {
    log.debug("Loading reference data.");

    ReferenceDataResponse response =
        restTemplate
            .getForEntity(
                UriComponentsBuilder.newInstance()
                    .path("/")
                    .pathSegment("reference-data", "BADGE")
                    .toUriString(),
                ReferenceDataResponse.class)
            .getBody();

    return response.getData();
  }
}
