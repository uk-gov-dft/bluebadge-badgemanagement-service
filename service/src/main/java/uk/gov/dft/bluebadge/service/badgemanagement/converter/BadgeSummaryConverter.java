package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeSummary;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.RefData;
import uk.gov.dft.bluebadge.service.badgemanagement.service.RefDataGroupEnum;
import uk.gov.dft.bluebadge.service.badgemanagement.service.exception.BadRequestException;

public class BadgeSummaryConverter implements BiConverter<BadgeEntity, BadgeSummary> {
  @Override
  public BadgeEntity convertToEntity(BadgeSummary model) throws BadRequestException {
    return null;
  }

  @Override
  public BadgeSummary convertToModel(BadgeEntity entity) {
    BadgeSummary model = new BadgeSummary();
    model.setBadgeNumber(entity.getBadgeNo());
    model.setExpiryDate(entity.getExpiryDate());
    model.setLocalAuthorityCode(entity.getLocalAuthorityId());
    model.setLocalAuthorityName("NOT IMPLEMENTED");
    model.setName(entity.getHolderName());
    model.setNino(entity.getNino());
    model.setPartyTypeCode(entity.getPartyCode());
    model.setPartyTypeDescription(
        RefData.getDescription(RefDataGroupEnum.PARTY, entity.getPartyCode()));
    model.setStatusCode(entity.getBadgeStatus());
    model.setStatusDescription(
        RefData.getDescription(RefDataGroupEnum.STATUS, entity.getBadgeStatus()));
    return model;
  }
}
