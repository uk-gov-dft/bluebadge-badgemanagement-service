package uk.gov.dft.bluebadge.service.badgemanagement.client.printservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.Batch;

@Slf4j
@Service
public class PrintServiceApiClient {

  private final RestTemplate restTemplate;

  @Autowired
  PrintServiceApiClient(@Qualifier("printServiceRestTemplate") RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /** Send Print Batch Request. */
  public void printBatch(Batch batch) {
    log.debug("Print batch.");

    String url =
        UriComponentsBuilder.newInstance().path("/").pathSegment("printBatch").toUriString();
    restTemplate.postForEntity(url, batch, CommonResponse.class);
  }
}
