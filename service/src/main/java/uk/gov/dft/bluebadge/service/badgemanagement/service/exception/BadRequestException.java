package uk.gov.dft.bluebadge.service.badgemanagement.service.exception;

import com.google.common.collect.Lists;
import java.util.List;
import org.springframework.http.HttpStatus;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Error;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.ErrorErrors;

public class BadRequestException extends ServiceException {

  /**
   * Bad request with a list of field level (body) validation errors.
   *
   * @param fieldErrors The errors.
   */
  public BadRequestException(List<ErrorErrors> fieldErrors) {
    this(new Error().errors(fieldErrors));
  }

  /**
   * Bad request with a single field level (body) validation error.
   *
   * @param fieldError The error.
   */
  public BadRequestException(ErrorErrors fieldError) {
    this(Lists.newArrayList(fieldError));
  }

  /**
   * Bad request with class level validation error, query param validation, for example.
   *
   * @param requestError The error.
   */
  public BadRequestException(Error requestError) {
    super(HttpStatus.BAD_REQUEST);
    commonResponse.setError(requestError);
  }
}
