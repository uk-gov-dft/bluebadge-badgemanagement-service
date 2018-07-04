package uk.gov.dft.bluebadge.service.badgemanagement.service.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.CommonResponse;

public class NotFoundException extends ServiceException {

  @Override
  public ResponseEntity<CommonResponse> getResponse() {
    return ResponseEntity.status(NOT_FOUND).body(commonResponse);
  }

  public enum Operation {
    DELETE("delete"),
    UPDATE("update"),
    RETRIEVE("retrieve");

    private String description;

    Operation(String description) {
      this.description = description;
    }
  }

  public NotFoundException(String objectName, Operation operation) {
    super(HttpStatus.NOT_FOUND);
    commonResponse.getError().setMessage("NotFound." + objectName);
    commonResponse
        .getError()
        .setReason("Could not " + operation.description + " " + objectName + ".");
  }
}
