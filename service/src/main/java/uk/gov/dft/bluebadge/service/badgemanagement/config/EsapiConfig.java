package uk.gov.dft.bluebadge.service.badgemanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import uk.gov.dft.bluebadge.service.badgemanagement.esapi.EsapiFilter;

@Configuration
public class EsapiConfig {

  @Bean
  @Order(1000)
  public EsapiFilter getEsapiFilter() {
    return new EsapiFilter();
  }
}
