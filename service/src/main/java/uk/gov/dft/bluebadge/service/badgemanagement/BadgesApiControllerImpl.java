package uk.gov.dft.bluebadge.service.badgemanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import uk.gov.dft.bluebadge.service.badgemanagement.controller.BadgeApi;
import uk.gov.dft.bluebadge.service.badgemanagement.converter.BadgeConverter;
import uk.gov.dft.bluebadge.service.badgemanagement.service.BadgeManagementService;

@Controller
public class BadgesApiControllerImpl implements BadgeApi {

  private BadgeManagementService service;
  private BadgeConverter badgeConverter = new BadgeConverter();
  private ObjectMapper objectMapper;
  private HttpServletRequest request;

  @SuppressWarnings("unused")
  @Autowired
  public BadgesApiControllerImpl(
      ObjectMapper objectMapper, HttpServletRequest request, BadgeManagementService service) {
    this.objectMapper = objectMapper;
    this.request = request;
    this.service = service;
  }

  //@Override
  public Optional<ObjectMapper> getObjectMapper() {
    return Optional.ofNullable(objectMapper);
  }

  //@Override
  public Optional<HttpServletRequest> getRequest() {
    return Optional.ofNullable(request);
  }
}
