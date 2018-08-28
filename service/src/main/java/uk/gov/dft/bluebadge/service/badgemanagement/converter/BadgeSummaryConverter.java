package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.common.converter.ToModelConverter;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeSummary;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;

@Component
public class BadgeSummaryConverter implements ToModelConverter<BadgeEntity, BadgeSummary> {

  @Override
  public BadgeSummary convertToModel(BadgeEntity entity) {
    BadgeSummary model = new BadgeSummary();
    model.setBadgeNumber(entity.getBadgeNo());
    model.setExpiryDate(entity.getExpiryDate());
    model.setLocalAuthorityShortCode(entity.getLocalAuthorityShortCode());
    model.setName(entity.getHolderName());
    model.setNino(entity.getNino());
    model.setPostCode(entity.getContactPostcode());
    model.setPartyTypeCode(entity.getPartyCode());
    model.setStatusCode(entity.getBadgeStatus().name());
    return model;
  }
}
