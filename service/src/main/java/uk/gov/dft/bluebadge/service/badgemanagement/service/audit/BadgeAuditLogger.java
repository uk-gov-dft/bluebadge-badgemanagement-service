package uk.gov.dft.bluebadge.service.badgemanagement.service.audit;

import java.util.List;
import lombok.Getter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.common.logging.LogEventBuilder;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

@Component
public class BadgeAuditLogger {
  @Getter
  enum AuditEventFields {
    CREATE_FIELDS(
        "badgeOrderRequest.localAuthorityShortCode",
        "localAuthorityRefData.localAuthorityMetaData.nation",
        "badgeOrderRequest.eligibilityCode",
        "badgeOrderRequest.party.typeCode",
        "badgeOrderRequest.startDate",
        "badgeOrderRequest.expiryDate",
        "issuedDate",
        "badgeOrderRequest.applicationDate",
        "badgeOrderRequest.applicationChannelCode",
        "createdBadgeNumbers",
        "badgeOrderRequest.numberOfBadges"),
    CANCEL_FIELDS(
        "cancelBadgeParams.localAuthorityShortCode",
        "cancelBadgeParams.cancelReasonCode",
        "cancellationDate");

    private final String[] fields;

    AuditEventFields(String... fields) {
      this.fields = fields;
    }
  }

  private ReferenceDataService referenceDataService;

  @Autowired
  BadgeAuditLogger(ReferenceDataService referenceDataService) {
    this.referenceDataService = referenceDataService;
  }

  public void logCancelAuditMessage(CancelBadgeParams request, Logger log) {
    BadgeCancelledAuditData badgeCancelledAuditData =
        BadgeCancelledAuditData.builder().cancelBadgeParams(request).build();

    LogEventBuilder.builder()
        .forObject(badgeCancelledAuditData)
        .withLogger(log)
        .withFields(AuditEventFields.CANCEL_FIELDS.getFields())
        .forEvent(LogEventBuilder.AuditEvent.BADGE_CANCELLED)
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
        .forEvent(LogEventBuilder.AuditEvent.BADGE_ORDERED)
        .withFields(AuditEventFields.CREATE_FIELDS.getFields())
        .log();
  }
}
