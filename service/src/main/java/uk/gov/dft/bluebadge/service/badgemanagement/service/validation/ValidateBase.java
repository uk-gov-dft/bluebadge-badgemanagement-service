package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

import java.util.List;

public abstract class ValidateBase {

  protected abstract ReferenceDataService getReferenceDataService();

  void validateRefData(
      RefDataGroupEnum group,
      ValidationKeyEnum validationKeyEnum,
      String value,
      List<ErrorErrors> errors) {
    if (null == value) return;

    if (!getReferenceDataService().groupContainsKey(group, value)) {
      errors.add(validationKeyEnum.getFieldErrorInstance());
    }
  }
}
