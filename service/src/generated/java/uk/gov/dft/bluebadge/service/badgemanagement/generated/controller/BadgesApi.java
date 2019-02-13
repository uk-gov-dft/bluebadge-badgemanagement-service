/**
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen Do not edit the class manually.
 */

package uk.gov.dft.bluebadge.service.badgemanagement.generated.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeCancelRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeNumberResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeNumbersResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeReplaceRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeResponse;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgesResponse;
import uk.gov.dft.bluebadge.service.badgemanagement.model.PrintBatchRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.Optional;

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
    notes = "Cancel a badge with immediate effect.",
    tags = {
      "badges",
    }
  )
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Badge cancellation requested.")})
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
    value = "Find a Blue Badge given the specified query parameters",
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
        message = "Response when Blue Badges can (or can not) be found. ",
        response = BadgesResponse.class
      ),
      @ApiResponse(code = 200, message = "Unexpected error.", response = CommonResponse.class)
    }
  )
  @RequestMapping(
    value = "/badges",
    produces = {"application/json"},
    method = RequestMethod.GET
  )
  default ResponseEntity<BadgesResponse> findBlueBadge(
    @Size(max = 100)
    @ApiParam(value = "Search the badge holder's name.")
    @Valid
    @RequestParam(value = "name", required = false)
      Optional<String> name,
    @Size(max = 20)
    @ApiParam(value = "A valid postcode with or without spaces.")
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
    notes =
      "Place an order for a new badge, which will add a new record to the central record immediately and result in a fulfilment request being sent to the printer with no further intervention required.",
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
    notes =
      "Cancel an existing badge and order a new badge with the same details (including expiry date), with a start date of today.",
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
    value = "Retrieve a Blue Badge given the Blue Badge number parameter.",
    nickname = "retrieveBlueBadge",
    notes = "Retrieves a Blue Badge given the blueBadgeNumber. ",
    response = BadgeResponse.class,
    tags = {
      "badges",
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        code = 200,
        message = "Response when Blue Badge can be retrieved. ",
        response = BadgeResponse.class
      ),
      @ApiResponse(
        code = 404,
        message = "A Blue Badge cannot be found given the parameters specified."
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

  @ApiOperation(
    value = "Print a batch",
    nickname = "printBatch",
    notes = "Request print batch.",
    tags = {
      "badges",
    }
  )
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Batch print requested ")})
  @RequestMapping(value = "/badges/print-batch", method = RequestMethod.POST)
  default ResponseEntity<Void> printBatch(
    @ApiParam(value = "") @Valid @RequestBody PrintBatchRequest printBadgeRequest) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
      }
    } else {
      log.warn(
        "ObjectMapper or HttpServletRequest not configured in default BadgesApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(
    value = "Process results of print batches",
    nickname = "collectBatches",
    notes = "Process print batch results",
    tags = {
      "badges",
    }
  )
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Print batch results processed.")})
  @RequestMapping(value = "/badges/collect-batches", method = RequestMethod.POST)
  ResponseEntity<Void> collectBatches();

  @ApiOperation(
    value = "Reprint a batch",
    nickname = "reprintBatch",
    notes = "Reprints a batch",
    tags = {
      "batches",
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        code = 200,
        message = "Request to reprint was successful."
      ),
      @ApiResponse(
        code = 404,
        message = "A Batch cannot be found given the parameters specified."
      ),
      @ApiResponse(
        code = 401,
        message = "The request is unauthorised."
      )
    }
  )
  @RequestMapping(
    value = "/badges/print-batch/{batchId}",
    produces = {"application/json"},
    method = RequestMethod.POST
  )
  default ResponseEntity<Void> reprintBatch(
    @ApiParam(value = "A valid batch number.", required = true) @PathVariable("batchId")
      String batchId) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
      }
    } else {
      log.warn(
        "ObjectMapper or HttpServletRequest not configured in default BadgesApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}
