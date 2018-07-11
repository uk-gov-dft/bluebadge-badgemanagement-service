package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.common.converter.ToModelConverter;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeSummary;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

@Component
public class BadgeSummaryConverter implements ToModelConverter<BadgeEntity, BadgeSummary> {

  private final ReferenceDataService referenceDataService;

  @Autowired
  public BadgeSummaryConverter(ReferenceDataService referenceDataService) {
    this.referenceDataService = referenceDataService;
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
        referenceDataService.getDescription(RefDataGroupEnum.PARTY, entity.getPartyCode()));
    model.setStatusCode(entity.getBadgeStatus().name());
    model.setStatusDescription(
        referenceDataService.getDescription(
            RefDataGroupEnum.STATUS, entity.getBadgeStatus().name()));
    return model;
  }
}
