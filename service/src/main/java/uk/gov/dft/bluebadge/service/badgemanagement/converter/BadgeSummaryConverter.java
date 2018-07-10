package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeSummary;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

public class BadgeSummaryConverter implements ToModelConverter<BadgeEntity, BadgeSummary> {

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
        ReferenceDataService.getDescription(RefDataGroupEnum.PARTY, entity.getPartyCode()));
    model.setStatusCode(entity.getBadgeStatus().name());
    model.setStatusDescription(
        ReferenceDataService.getDescription(
            RefDataGroupEnum.STATUS, entity.getBadgeStatus().name()));
    return model;
  }
}
