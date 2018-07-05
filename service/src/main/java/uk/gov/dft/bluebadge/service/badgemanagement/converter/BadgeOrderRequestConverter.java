package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import org.apache.commons.lang3.StringUtils;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Contact;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Organisation;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Person;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;

public class BadgeOrderRequestConverter implements BiConverter<BadgeEntity, BadgeOrderRequest> {

  @Override
  public BadgeEntity convertToEntity(BadgeOrderRequest model) {
    boolean isPerson = model.getParty().getTypeCode().equals("PERSON");
    Person person;
    Organisation org;
    Contact contact = model.getParty().getContact();
    if (isPerson) {
      person = model.getParty().getPerson();
      org = new Organisation();
    } else {
      org = model.getParty().getOrganisation();
      person = new Person();
    }

    return BadgeEntity.builder()
        .partyCode(model.getParty().getTypeCode())
        .localAuthorityId(model.getLocalAuthorityId())
        .localAuthorityRef(model.getLocalAuthorityRef())
        .appDateTime(model.getApplicationDate())
        .appChannelCode(model.getApplicationChannelCode())
        .startDate(model.getStartDate())
        .expiryDate(model.getExpiryDate())
        .eligibilityCode(model.getEligibilityCode())
        .deliverToCode(model.getDeliverToCode())
        .deliverOptionCode(model.getDeliveryOptionCode())
        .holderName(isPerson ? person.getBadgeHolderName() : org.getBadgeHolderName())
        .nino(person.getNino())
        .dob(person.getDob())
        .genderCode(person.getGenderCode())
        .contactName(contact.getFullName())
        .contactBuildingStreet(contact.getBuildingStreet())
        .contactLine2(contact.getLine2())
        .contactTownCity(contact.getTownCity())
        .contactEmailAddress(contact.getEmailAddress())
        .contactPostcode(
            null == contact.getPostCode()
                ? null
                : StringUtils.removeAll(contact.getPostCode().toUpperCase(), " "))
        .primaryPhoneNo(contact.getPrimaryPhoneNumber())
        .secondaryPhoneNo(contact.getSecondaryPhoneNumber())
        .numberOfBadges(null == model.getNumberOfBadges() ? 1 : model.getNumberOfBadges())
        .build();
    // TODO image link -> file
  }

  @Override
  public BadgeOrderRequest convertToModel(BadgeEntity entity) {
    return null;
  }
}
