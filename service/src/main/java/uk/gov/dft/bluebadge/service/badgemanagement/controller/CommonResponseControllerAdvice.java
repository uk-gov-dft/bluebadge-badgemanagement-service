package uk.gov.dft.bluebadge.service.badgemanagement.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import uk.gov.dft.bluebadge.common.api.CommonResponseEntityExceptionHandler;

@ControllerAdvice
public class CommonResponseControllerAdvice extends CommonResponseEntityExceptionHandler {}