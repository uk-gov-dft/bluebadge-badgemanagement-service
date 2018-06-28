/**
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen Do not edit the class manually.
 */
package uk.gov.dft.bluebadge.service.badgemanagement.generated.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeCancelRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeNumberResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeNumbersResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeReplaceRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgesResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.CommonResponse;

@Api(value = "Badges", description = "the Badges API")
public interface BadgesApi {

  Logger log = LoggerFactory.getLogger(BadgesApi.class);

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
    value = "Request cancel badge",
    nickname = "cancelBlueBadge",
    notes = "Request cancellation of a badge.",
    tags = {
      "badges",
    }
  )
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Badge replacement requested.")})
  @RequestMapping(value = "/badges/{badgeNumber}/cancellations", method = RequestMethod.POST)
  default ResponseEntity<Void> cancelBlueBadge(
      @ApiParam(value = "A valid badge number.", required = true) @PathVariable("badgeNumber")
          String badgeNumber,
      @ApiParam(value = "") @Valid @RequestBody BadgeCancelRequest badgeCancel) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default BadgesApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(
    value = "Delete a badge",
    nickname = "deleteBlueBadge",
    notes = "Delete a Blue Badge.",
    tags = {
      "badges",
    }
  )
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Deleted.")})
  @RequestMapping(value = "/badges/{badgeNumber}", method = RequestMethod.DELETE)
  default ResponseEntity<Void> deleteBlueBadge(
      @ApiParam(value = "A valid badge number.", required = true) @PathVariable("badgeNumber")
          String badgeNumber) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default BadgesApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(
    value = "Find a blue badge given the specified query parameters",
    nickname = "findBlueBadge",
    notes =
        "By passing in appropriate options, you can search for available badges in the system.  Options are a partial match and all submitted options must be matched. At least 1 parameter must be provided. ",
    response = BadgesResponse.class,
    tags = {
      "badges",
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        code = 200,
        message = "Response when blue badges can (or can not) be found. ",
        response = BadgesResponse.class
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
      @ApiParam(value = "Search the badge holders name")
          @Valid
          @RequestParam(value = "name", required = false)
          Optional<String> name,
      @ApiParam(value = "A valid postcode with or without spaces")
          @Valid
          @RequestParam(value = "postCode", required = false)
          Optional<String> postCode) {
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
          "ObjectMapper or HttpServletRequest not configured in default BadgesApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(
    value = "Order badges",
    nickname = "orderBlueBadges",
    notes = "Order One or more badges.",
    response = BadgeNumbersResponse.class,
    tags = {
      "badges",
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "Badges created.", response = BadgeNumbersResponse.class)
    }
  )
  @RequestMapping(
    value = "/badges",
    produces = {"application/json"},
    method = RequestMethod.POST
  )
  default ResponseEntity<BadgeNumbersResponse> orderBlueBadges(
      @ApiParam(value = "") @Valid @RequestBody BadgeOrderRequest badgeOrder) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        try {
          return new ResponseEntity<>(
              getObjectMapper().get().readValue("\"\"", BadgeNumbersResponse.class),
              HttpStatus.NOT_IMPLEMENTED);
        } catch (IOException e) {
          log.error("Couldn't serialize response for content type application/json", e);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default BadgesApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(
    value = "Request replacement badge",
    nickname = "replaceBlueBadge",
    notes = "Request a replacement badge.",
    response = BadgeNumberResponse.class,
    tags = {
      "badges",
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        code = 200,
        message = "Badge replacement complete.  New badge number returned. ",
        response = BadgeNumberResponse.class
      )
    }
  )
  @RequestMapping(value = "/badges/{badgeNumber}/replacements", method = RequestMethod.POST)
  default ResponseEntity<BadgeNumberResponse> replaceBlueBadge(
      @ApiParam(value = "A valid badge number.", required = true) @PathVariable("badgeNumber")
          String badgeNumber,
      @ApiParam(value = "") @Valid @RequestBody BadgeReplaceRequest badgeReplace) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        try {
          return new ResponseEntity<>(
              getObjectMapper().get().readValue("\"\"", BadgeNumberResponse.class),
              HttpStatus.NOT_IMPLEMENTED);
        } catch (IOException e) {
          log.error("Couldn't serialize response for content type application/json", e);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default BadgesApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(
    value = "Retrieve a blue badge given the blue badge number parameter.",
    nickname = "retrieveBlueBadge",
    notes = "Retrieves a blue-badge given the blueBadgeNumber. ",
    response = BadgeResponse.class,
    tags = {
      "badges",
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
      )
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
          "ObjectMapper or HttpServletRequest not configured in default BadgesApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}