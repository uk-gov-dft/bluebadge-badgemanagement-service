/**
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen Do not edit the class manually.
 */
package uk.gov.dft.bluebadge.service.badgemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.dft.bluebadge.model.badgemanagement.BadgeResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.BadgesResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.CommonResponse;

@Api(value = "Badge", description = "the Badge API")
public interface BadgeApi {

  Logger log = LoggerFactory.getLogger(BadgeApi.class);

  default Optional<ObjectMapper> getObjectMapper() {
    return Optional.empty();
  }

  default Optional<HttpServletRequest> getRequest() {
    return Optional.empty();
  }

  default Optional<String> getAcceptHeader() {
    return getRequest().map(r -> r.getHeader("Accept"));
  }

  @ApiOperation(
    value = "Find a blue badge given the specified query parameters",
    nickname = "findBlueBadge",
    notes =
        "By passing in appropriate options, you can search for available badges in the system.  Options are a partial match and all submitted options must be matched. At least 1 parameter must be provided. ",
    response = BadgesResponse.class,
    tags = {
      "badge",
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        code = 200,
        message = "Response when blue badges can be found. ",
        response = BadgesResponse.class
      ),
      @ApiResponse(
        code = 404,
        message = "A blue badge cannot be found given the parameters specified"
      ),
      @ApiResponse(code = 200, message = "Unexpected error", response = CommonResponse.class)
    }
  )
  @RequestMapping(
    value = "/badges",
    produces = {"application/json"},
    method = RequestMethod.GET
  )
  default ResponseEntity<BadgesResponse> findBlueBadge(
      @ApiParam(value = "A valid badge number.")
          @Valid
          @RequestParam(value = "badgeNumber", required = false)
          Optional<String> badgeNumber,
      @ApiParam(value = "Search the badge holders name")
          @Valid
          @RequestParam(value = "name", required = false)
          Optional<String> name,
      @ApiParam(value = "A valid National Insurance number")
          @Valid
          @RequestParam(value = "ni", required = false)
          Optional<String> ni) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        try {
          return new ResponseEntity<>(
              getObjectMapper().get().readValue("\"\"", BadgesResponse.class),
              HttpStatus.NOT_IMPLEMENTED);
        } catch (IOException e) {
          log.error("Couldn't serialize response for content type application/json", e);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default BadgeApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(
    value = "Retrieve a blue badge given the blue badge number parameter.",
    nickname = "retrieveBlueBadge",
    notes = "Retrieves a blue-badge given the blueBadgeNumber. ",
    response = BadgeResponse.class,
    tags = {
      "badge",
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        code = 200,
        message = "Response when blue badge can be retrieved. ",
        response = BadgeResponse.class
      ),
      @ApiResponse(
        code = 404,
        message = "A blue badge cannot be found given the parameters specified"
      ),
      @ApiResponse(code = 200, message = "Unexpected error", response = CommonResponse.class)
    }
  )
  @RequestMapping(
    value = "/badges/{badgeNumber}",
    produces = {"application/json"},
    method = RequestMethod.GET
  )
  default ResponseEntity<BadgeResponse> retrieveBlueBadge(
      @ApiParam(value = "A valid badge number.", required = true) @PathVariable("badgeNumber")
          String badgeNumber) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        try {
          return new ResponseEntity<>(
              getObjectMapper().get().readValue("\"\"", BadgeResponse.class),
              HttpStatus.NOT_IMPLEMENTED);
        } catch (IOException e) {
          log.error("Couldn't serialize response for content type application/json", e);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default BadgeApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
