package uk.gov.dft.bluebadge.service.badgemanagement.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import uk.gov.dft.bluebadge.common.api.CommonResponseEntityExceptionHandler;

@ControllerAdvice(annotations = {CommonResponse.class})
public class CommonResponseControllerAdvice extends CommonResponseEntityExceptionHandler {}
