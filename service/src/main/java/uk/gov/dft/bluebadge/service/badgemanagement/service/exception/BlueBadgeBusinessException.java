package uk.gov.dft.bluebadge.service.badgemanagement.service.exception;

import java.util.List;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.ErrorErrors;

public class BlueBadgeBusinessException extends Exception {
  private transient List<ErrorErrors> errorsList;

  public BlueBadgeBusinessException(List<ErrorErrors> errorsList) {
    this.errorsList = errorsList;
  }

  public List<ErrorErrors> getErrorsList() {
    return errorsList;
  }
}
