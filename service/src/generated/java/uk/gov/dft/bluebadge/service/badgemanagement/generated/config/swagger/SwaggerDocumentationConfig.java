package uk.gov.dft.bluebadge.service.badgemanagement.generated.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerDocumentationConfig {

  ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Blue Badge Management Service")
        .description("External API for local authorities to manage blue badges")
        .license("Apache 2.0")
        .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
        .termsOfServiceUrl("")
        .version("1.0.0")
        .contact(new Contact("", "", "rob.smallwood@does.not.exist"))
        .build();
  }

  @Bean
  public Docket customImplementation() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(
            RequestHandlerSelectors.basePackage(
                "uk.gov.dft.bluebadge.service.badgemanagement.generated.controller"))
        .build()
        .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
        .directModelSubstitute(java.time.OffsetDateTime.class, java.util.Date.class)
        .apiInfo(apiInfo());
  }
}
