package uk.gov.dft.bluebadge.service.badgemanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import uk.gov.dft.bluebadge.common.api.common.RestTemplateFactory;
import uk.gov.dft.bluebadge.common.api.common.ServiceConfiguration;
import uk.gov.dft.bluebadge.common.logging.LoggingAspect;

@Configuration
public class ApiConfig {

  @Validated
  @ConfigurationProperties("blue-badge.reference-data-service.service-host")
  @Bean
  public ServiceConfiguration referenceDataServiceConfiguration() {
    return new ServiceConfiguration();
  }

  /**
   * OAuth rest template configured with a token forwarding context. So if a bearer token is found
   * on the security context, then it is used for the rest template.
   */
  @Bean("referenceDataServiceRestTemplate")
  RestTemplate referenceDataServiceRestTemplate(
      ClientCredentialsResourceDetails clientCredentialsResourceDetails,
      ServiceConfiguration referenceDataServiceConfiguration) {
    return RestTemplateFactory.getClientRestTemplate(
        clientCredentialsResourceDetails, referenceDataServiceConfiguration);
  }

  @Validated
  @ConfigurationProperties("blue-badge.print-service.service-host")
  @Bean
  public ServiceConfiguration printServiceConfiguration() {
    return new ServiceConfiguration();
  }

  /**
   * OAuth rest template configured with a token forwarding context. So if a bearer token is found
   * on the security context, then it is used for the rest template.
   */
  @Bean("printServiceRestTemplate")
  RestTemplate printServiceRestTemplate(
      ClientCredentialsResourceDetails clientCredentialsResourceDetails,
      ServiceConfiguration printServiceConfiguration) {
    RestTemplate restTemplate =
        RestTemplateFactory.getClientRestTemplate(
            clientCredentialsResourceDetails, printServiceConfiguration);
    return restTemplate;
  }

  @Bean
  LoggingAspect getControllerLoggingAspect() {
    return new LoggingAspect();
  }
}
