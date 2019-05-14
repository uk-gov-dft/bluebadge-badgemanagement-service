package uk.gov.dft.bluebadge.service.badgemanagement.client.printservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.service.badgemanagement.ApplicationContextTests;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.PrintBatchRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.ProcessedBatch;
import uk.gov.dft.bluebadge.service.badgemanagement.client.printservice.model.ProcessedBatchesResponse;

@RunWith(MockitoJUnitRunner.class)
public class PrintServiceApiClientTest extends ApplicationContextTests {
  private static final String TEST_URI = "http://justtesting:7777/test";
  private static final String BASE_ENDPOINT = TEST_URI + "/";
  private static final String BATCH_TYPE = "STANDARD";
  private static final String FILENAME = "filename";

  private PrintServiceApiClient client;
  private MockRestServiceServer mockServer;
  private ObjectMapper om = new ObjectMapper();

  @Before
  public void setUp() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(TEST_URI));
    mockServer = MockRestServiceServer.bindTo(restTemplate).build();

    client = new PrintServiceApiClient(restTemplate);
  }

  @Test
  @SneakyThrows
  public void printBatch_shouldWork() {
    CommonResponse response = new CommonResponse();
    String responseBody = om.writeValueAsString(response);

    mockServer
        .expect(once(), requestTo(BASE_ENDPOINT + "printBatch"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(jsonPath("batchType", equalTo(BATCH_TYPE)))
        .andExpect(jsonPath("filename", equalTo(FILENAME)))
        .andExpect(jsonPath("badges", nullValue()))
        .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

    PrintBatchRequest request =
        PrintBatchRequest.builder().batchType(BATCH_TYPE).filename(FILENAME).badges(null).build();
    client.printBatch(request);
  }

  @Test
  @SneakyThrows
  public void printBatch_shouldThrowIllegalArgumentException_when302IsReceived() {
    CommonResponse response = new CommonResponse();
    String responseBody = om.writeValueAsString(response);

    mockServer
        .expect(once(), requestTo(BASE_ENDPOINT + "printBatch"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(jsonPath("batchType", equalTo(BATCH_TYPE)))
        .andExpect(jsonPath("filename", equalTo(FILENAME)))
        .andExpect(jsonPath("badges", nullValue()))
        .andRespond(withStatus(HttpStatus.FOUND));

    try {
      PrintBatchRequest request =
          PrintBatchRequest.builder().batchType(BATCH_TYPE).filename(FILENAME).badges(null).build();
      client.printBatch(request);
    } catch (IllegalArgumentException ex) {
      assertThat(ex.getMessage())
          .isEqualTo("Client Http Status received from print service must be 200 but was 302");
    }
  }

  @Test
  @SneakyThrows
  public void collectPrintBatchResults_shouldWork() {
    ProcessedBatchesResponse expectedResponseBody =
        ProcessedBatchesResponse.builder()
            .data(Lists.newArrayList(ProcessedBatch.builder().filename(FILENAME).build()))
            .build();
    String expectedResponseJsonBody = om.writeValueAsString(expectedResponseBody);

    mockServer
        .expect(once(), requestTo(BASE_ENDPOINT + "processed-batches"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(expectedResponseJsonBody, MediaType.APPLICATION_JSON));

    ProcessedBatchesResponse actualResponseBody = client.collectPrintBatchResults();
    assertThat(actualResponseBody).isEqualTo(expectedResponseBody);
  }

  @Test
  @SneakyThrows
  public void deleteBatchConfirmation_shouldWork() {
    CommonResponse response = new CommonResponse();
    String responseBody = om.writeValueAsString(response);

    mockServer
        .expect(once(), requestTo(BASE_ENDPOINT + "processed-batches/" + FILENAME))
        .andExpect(method(HttpMethod.DELETE))
        .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

    client.deleteBatchConfirmation(FILENAME);
  }
}
