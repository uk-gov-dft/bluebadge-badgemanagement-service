package uk.gov.dft.bluebadge.service.badgemanagement.service.audit;

import lombok.Getter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.common.logging.LogEventBuilder;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

import java.util.List;

@Component
public class BadgeAuditLogger {
  @Getter
  enum AuditEventFields {
    CREATE(
        LogEventBuilder.AuditEvent.BADGE_ORDERED,
        "badgeOrderRequest.localAuthorityShortCode",
        "localAuthorityRefData.nation",
        "badgeOrderRequest.eligibilityCode",
        "badgeOrderRequest.party.typeCode",
        "badgeOrderRequest.startDate",
        "badgeOrderRequest.expiryDate",
        "issuedDate",
        "badgeOrderRequest.applicationDate",
        "badgeOrderRequest.applicationChannelCode",
        "createdBadgeNumbers",
        "badgeOrderRequest.numberOfBadges"),
    CANCEL(
        LogEventBuilder.AuditEvent.BADGE_CANCELLED,
        "cancelBadgeParams.localAuthorityShortCode",
        "cancelBadgeParams.cancelReasonCode",
        "cancellationDate");

    private final LogEventBuilder.AuditEvent event;
    private final String[] fields;

    AuditEventFields(LogEventBuilder.AuditEvent event, String... fields) {
      this.event = event;
      this.fields = fields;
    }
  }

  private ReferenceDataService referenceDataService;

  @Autowired
  public BadgeAuditLogger(ReferenceDataService referenceDataService) {
    this.referenceDataService = referenceDataService;
  }

  public void logCancelAuditMessage(CancelBadgeParams request, Logger log) {
    BadgeCancelledAuditData badgeCancelledAuditData =
        BadgeCancelledAuditData.builder().cancelBadgeParams(request).build();

    LogEventBuilder.builder()
        .withLogger(log)
        .withFields(AuditEventFields.CANCEL.getFields())
        .forEvent(AuditEventFields.CANCEL.getEvent())
        .forObject(badgeCancelledAuditData)
        .log();
  }

  public void logCreateAuditMessage(
      BadgeOrderRequest badgeOrderRequest, List<String> createdBadgeNumbers, Logger log) {
    BadgeOrderedAuditData data =
        BadgeOrderedAuditData.builder()
            .badgeOrderRequest(badgeOrderRequest)
            .createdBadgeNumbers(createdBadgeNumbers)
            .localAuthorityRefData(
                referenceDataService.retrieveLocalAuthority(
                    badgeOrderRequest.getLocalAuthorityShortCode()))
            .build();
    LogEventBuilder.builder()
        .forObject(data)
        .withLogger(log)
        .forEvent(AuditEventFields.CREATE.getEvent())
        .withFields(AuditEventFields.CREATE.getFields())
        .log();
  }
}
