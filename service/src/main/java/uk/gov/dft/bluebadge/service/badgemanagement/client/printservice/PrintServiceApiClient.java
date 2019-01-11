package uk.gov.dft.bluebadge.service.badgemanagement.client.printservice;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.PrintBatchRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.ProcessedBatchesResponse;

@Slf4j
@Service
public class PrintServiceApiClient {

  private final RestTemplate restTemplate;

  @Autowired
  PrintServiceApiClient(@Qualifier("printServiceRestTemplate") RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /** Send Print PrintBatchRequest Request. */
  public void printBatch(PrintBatchRequest batch) {
    log.debug("Print batch.");

    String url =
        UriComponentsBuilder.newInstance().path("/").pathSegment("printBatch").toUriString();
    restTemplate.postForEntity(url, batch, CommonResponse.class);
  }

  public ProcessedBatchesResponse collectPrintBatchResults(){
    log.debug("Call collect batches print service endpoint.");

    String url =
        UriComponentsBuilder.newInstance().path("/").pathSegment("processed-batches").toUriString();
    return restTemplate.getForEntity(url, ProcessedBatchesResponse.class).getBody();
  }

  public void deleteBatchConfirmation(String confirmationFileName){
    log.info("Requesting delete confirmation file {} from print service.", confirmationFileName);

    String url =
        UriComponentsBuilder.newInstance().path("/").pathSegment("processed-batches", confirmationFileName).toUriString();
    restTemplate.delete(url);
  }
}
