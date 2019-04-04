package uk.gov.dft.bluebadge.service.badgemanagement.controller;

import java.io.IOException;
import java.time.LocalDate;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dft.bluebadge.common.service.exception.InternalServerException;
import uk.gov.dft.bluebadge.service.badgemanagement.service.BadgeManagementService;

@RestController
public class BadgesApiZipController {

  private final BadgeManagementService badgeService;

  @SuppressWarnings("unused")
  @Autowired
  public BadgesApiZipController(BadgeManagementService service) {
    this.badgeService = service;
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Void> handleAccessDeniedException(AccessDeniedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }

  @GetMapping(value = "/badges", produces = "application/zip")
  @PreAuthorize(
      "hasAuthority('PERM_VIEW_BADGE_DETAILS_ZIP') and @securityUtils.isAuthorisedLACode(#laShortCode)")
  public void retrieveBadgesByLa(
      @RequestParam(value = "laShortCode") String laShortCode, HttpServletResponse response) {
    String filename = LocalDate.now() + "_" + laShortCode.toUpperCase() + ".zip";
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename);
    response.setContentType("application/zip");
    try {
      badgeService.retrieveBadgesByLa(response.getOutputStream(), laShortCode.toUpperCase());
    } catch (IOException e) {
      throw new InternalServerException(
          "Failed creating badge zip file for " + laShortCode.toUpperCase(), e);
    }
  }
}
