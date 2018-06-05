package uk.gov.dft.bluebadge.client.badgemanagement.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import uk.gov.dft.bluebadge.client.badgemanagement.httpclient.RestTemplateFactory;
import uk.gov.dft.bluebadge.model.badgemanagement.BadgeResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.BadgesResponse;

import java.util.Collections;

@Service
public class BadgeManagementService {

  class Endpoints {
    // static final String GET_USER_BY_EMAIL_ENDPOINT = "/users?emailAddress={emailAddress}";
    // static final String CREATE_ENDPOINT = "/authorities/{authorityId}/users";
  }

  private RestTemplateFactory restTemplateFactory;
  // private ServiceConfiguration serviceConfiguration;

  @Autowired
  public BadgeManagementService(RestTemplateFactory restTemplateFactory) {
    // this.serviceConfiguration = serviceConfiguration;
    this.restTemplateFactory = restTemplateFactory;
  }

  public BadgesResponse searchBadges(String name, String niNumber, String badgeNumber) {

    // TODO mock version.
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

    ResponseEntity<BadgesResponse> response =
        restTemplateFactory
            .getInstance()
            .exchange(
                "http://virtserver.swaggerhub.com:80/uk-gov-dft/blue-badge/1.0.0/badges?badgeNumber=ddd&name=ff&ni=gg",
                HttpMethod.GET,
                entity,
                BadgesResponse.class);
    return response.getBody();
  }

  public BadgeResponse retrieveBadge(String badgeNumber) {

    // TODO mock version.
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

    ResponseEntity<BadgeResponse> response =
        restTemplateFactory
            .getInstance()
            .exchange(
                "http://virtserver.swaggerhub.com:80/uk-gov-dft/blue-badge/1.0.0/badges/abc",
                HttpMethod.GET,
                entity,
                BadgeResponse.class,
                "abc");
    return response.getBody();
  }
}
