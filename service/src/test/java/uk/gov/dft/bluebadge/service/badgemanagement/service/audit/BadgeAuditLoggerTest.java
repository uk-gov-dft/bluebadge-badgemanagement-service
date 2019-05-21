package uk.gov.dft.bluebadge.service.badgemanagement.service.audit;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import uk.gov.dft.bluebadge.common.logging.LogEventBuilder;
import uk.gov.dft.bluebadge.common.service.enums.EligibilityType;
import uk.gov.dft.bluebadge.common.service.enums.Nation;
import uk.gov.dft.bluebadge.service.badgemanagement.model.BadgeOrderRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Party;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model.LocalAuthorityRefData.LocalAuthorityMetaData;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.CancelBadgeParams;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

public class BadgeAuditLoggerTest {

  @Mock private ReferenceDataService referenceDataService;
  @Mock private Logger log;
  private BadgeAuditLogger badgeAuditLogger;

  public BadgeAuditLoggerTest() {
    MockitoAnnotations.initMocks(this);
    when(log.isInfoEnabled()).thenReturn(true);
    badgeAuditLogger = new BadgeAuditLogger(referenceDataService);
  }

  @Test
  public void logCancelAuditMessage() {
    badgeAuditLogger.logCancelAuditMessage(
        CancelBadgeParams.builder().cancelReasonCode("DDD").localAuthorityShortCode("ABC").build(),
        log);
    verify(log, times(1)).info(LogEventBuilder.AuditEvent.BADGE_CANCELLED.name());
  }

  @Test
  public void logCreateAuditMessage() {

    badgeAuditLogger.logCreateAuditMessage(getPopulatedBadgeOrderRequest(), null, log);
    verify(log, times(1)).info(LogEventBuilder.AuditEvent.BADGE_ORDERED.name());
  }

  @Test
  public void fieldsInObject_BadgeOrdererAuditData() {
    ExpressionParser parser = new SpelExpressionParser();

    List<String> createdBadges = new ArrayList<>();
    createdBadges.add("123456");
    // Given a full set of create audit source data
    BadgeOrderedAuditData badgeOrderedAuditData =
        BadgeOrderedAuditData.builder()
            .badgeOrderRequest(getPopulatedBadgeOrderRequest())
            .localAuthorityRefData(getLocalAuthorityRefData())
            .createdBadgeNumbers(createdBadges)
            .build();

    StandardEvaluationContext context = new StandardEvaluationContext(badgeOrderedAuditData);
    // The log will be populated with every specified field
    for (String field : BadgeAuditLogger.AuditEventFields.CREATE_FIELDS.getFields()) {
      if (null == parser.parseExpression(field).getValue(context)) {
        fail("Field not in object:" + field);
      }
    }
  }

  @Test
  public void fieldsInObject_BadgeCancelledAuditData() {
    ExpressionParser parser = new SpelExpressionParser();

    // Given a full set of cancel audit source data
    BadgeCancelledAuditData badgeCancelledAuditData =
        BadgeCancelledAuditData.builder()
            .cancelBadgeParams(
                CancelBadgeParams.builder()
                    .localAuthorityShortCode("ABC")
                    .cancelReasonCode("REASON")
                    .build())
            .build();

    StandardEvaluationContext context = new StandardEvaluationContext(badgeCancelledAuditData);
    // The log will be populated with every specified field
    for (String field : BadgeAuditLogger.AuditEventFields.CANCEL_FIELDS.getFields()) {
      if (null == parser.parseExpression(field).getValue(context)) {
        fail("Field not in object:" + field);
      }
    }
  }

  private BadgeOrderRequest getPopulatedBadgeOrderRequest() {
    return new BadgeOrderRequest().builder()
        .applicationChannelCode("CHANNEL")
        .applicationDate(LocalDate.now())
        .eligibilityCode(EligibilityType.WALKD)
        .startDate(LocalDate.now())
        .expiryDate(LocalDate.now())
        .localAuthorityShortCode("ABC")
        .numberOfBadges(1)
        .party(new Party().typeCode("ORG"))
        .build();
  }

  private LocalAuthorityRefData getLocalAuthorityRefData() {
    LocalAuthorityMetaData meta = new LocalAuthorityMetaData();
    meta.setNation(Nation.SCO);
    LocalAuthorityRefData data = new LocalAuthorityRefData();
    data.setLocalAuthorityMetaData(meta);
    return data;
  }
}
