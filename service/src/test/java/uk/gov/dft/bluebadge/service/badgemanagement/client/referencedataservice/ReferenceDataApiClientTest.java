package uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import uk.gov.dft.bluebadge.service.badgemanagement.ApplicationContextTests;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model.ReferenceData;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model.ReferenceDataResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceDataApiClientTest extends ApplicationContextTests {
  private static final String TEST_URI = "http://justtesting:7777/test/";
  private static final String BASE_ENDPOINT = TEST_URI + "reference-data";

  private ReferenceDataApiClient client;
  private MockRestServiceServer mockServer;
  private ObjectMapper om = new ObjectMapper();

  @Before
  public void setUp() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(TEST_URI));
    mockServer = MockRestServiceServer.bindTo(restTemplate).build();

    client = new ReferenceDataApiClient(restTemplate);
  }

  @Test
  public void retrieveReferenceData() throws Exception {
    ReferenceData referenceData1 = buildReferenceData(1);
    ReferenceData referenceData2 = buildReferenceData(2);
    ReferenceData referenceData3 = buildReferenceData(3);
    List<ReferenceData> referenceDataList =
        Lists.newArrayList(referenceData1, referenceData2, referenceData3);
    ReferenceDataResponse response = new ReferenceDataResponse();
    response.setData(referenceDataList);
    String responseBody = om.writeValueAsString(response);

    mockServer
        .expect(once(), requestTo(BASE_ENDPOINT + "/BADGE"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

    List<ReferenceData> result = client.retrieveReferenceData();
    assertThat(result).isEqualTo(referenceDataList);
  }

  private ReferenceData buildReferenceData(int i) {
    ReferenceData data = new ReferenceData();

    data.setDescription("description" + 1);
    data.setDisplayOrder(i);
    data.setGroupDescription("groupDescription" + i);
    data.setGroupShortCode("groupShortCode" + i);
    data.setShortCode("shortCode" + i);
    data.setSubgroupDescription("subGroupDescription" + i);
    data.setSubgroupShortCode("subGroupShortCode" + i);
    return data;
  }
}
