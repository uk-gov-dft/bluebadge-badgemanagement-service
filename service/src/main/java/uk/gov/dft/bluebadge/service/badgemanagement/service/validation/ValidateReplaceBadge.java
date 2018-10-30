package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import static uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum.DELIVER_TO;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum.DELIVERY_OPTIONS;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum.REPLACE;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.INVALID_DELIVER_TO_CODE;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.INVALID_DELIVER_OPTION_CODE;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.validation.ValidationKeyEnum.REPLACE_INVALID_REASON;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.ReplaceBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

@Component
@Slf4j
public class ValidateReplaceBadge extends ValidateBase {

  private final ReferenceDataService referenceDataService;

  @Autowired
  public ValidateReplaceBadge(ReferenceDataService referenceDataService) {
    this.referenceDataService = referenceDataService;
  }

  @Override
  protected ReferenceDataService getReferenceDataService() {
    return referenceDataService;
  }

  public void validateRequest(ReplaceBadgeParams params) {
    log.debug("Validating replace request for {}", params.getBadgeNumber());
    List<ErrorErrors> errors = new ArrayList<>();
    validateRefData(DELIVER_TO, INVALID_DELIVER_TO_CODE, params.getDeliveryCode(), errors);
    validateRefData(DELIVERY_OPTIONS, INVALID_DELIVER_OPTION_CODE, params.getDeliveryOptionCode(), errors);
    validateRefData(REPLACE, REPLACE_INVALID_REASON, params.getReasonCode(), errors);

    if (!errors.isEmpty()) {
      throw new BadRequestException(errors);
    }
  }
  
}
