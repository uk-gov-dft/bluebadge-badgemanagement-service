package uk.gov.dft.bluebadge.service.badgemanagement.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import uk.gov.dft.bluebadge.common.api.CommonResponseEntityExceptionHandler;
import uk.gov.dft.bluebadge.common.api.common.CommonResponseHandler;

@ControllerAdvice(annotations = CommonResponseHandler.class)
public class CommonResponseControllerAdvice extends CommonResponseEntityExceptionHandler {}
