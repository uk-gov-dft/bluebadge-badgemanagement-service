package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import uk.gov.dft.bluebadge.common.converter.ToModelConverter;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Badge;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Contact;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Organisation;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Party;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Person;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;

@SuppressWarnings("squid:CallToDeprecatedMethod")
public class BadgeConverter implements ToModelConverter<BadgeEntity, Badge> {

  @Override
  public Badge convertToModel(BadgeEntity entity) {
    Badge badge = new Badge();
    // Populate common parts of model first.
    badge.setApplicationChannelCode(entity.getAppChannelCode());
    badge.setApplicationDate(entity.getAppDate());
    badge.setBadgeNumber(entity.getBadgeNo());
    badge.setCancelReasonCode(entity.getCancelReasonCode());
    badge.setEligibilityCode(entity.getEligibilityCode());
    badge.setExpiryDate(entity.getExpiryDate());
    badge.setImageLink(entity.getImageLink());
    badge.setLocalAuthorityId(entity.getLocalAuthorityId());
    badge.setLocalAuthorityShortCode(entity.getLocalAuthorityShortCode());
    badge.setLocalAuthorityRef(entity.getLocalAuthorityRef());
    badge.setOrderDate(entity.getOrderDate());
    badge.setStartDate(entity.getStartDate());
    badge.setStatusCode(entity.getBadgeStatus().toString());

    Party party = new Party();
    badge.setParty(party);
    Contact contact = new Contact();
    party.setContact(contact);
    party.setTypeCode(entity.getPartyCode());

    // Populate contact
    contact.setPostCode(entity.getContactPostcode());
    contact.setTownCity(entity.getContactTownCity());
    contact.setFullName(entity.getContactName());
    contact.setEmailAddress(entity.getContactEmailAddress());
    contact.setBuildingStreet(entity.getContactBuildingStreet());
    contact.setLine2(entity.getContactLine2());
    contact.setPrimaryPhoneNumber(entity.getPrimaryPhoneNo());
    contact.setSecondaryPhoneNumber(entity.getSecondaryPhoneNo());

    // Party type specific stuff
    if (entity.isPerson()) {
      Person person = new Person();
      party.setPerson(person);
      person.setGenderCode(entity.getGenderCode());
      person.setDob(entity.getDob());
      person.setBadgeHolderName(entity.getHolderName());
      person.setNino(entity.getNino());
    } else {
      Organisation org = new Organisation();
      party.setOrganisation(org);
      org.setBadgeHolderName(entity.getHolderName());
    }

    return badge;
  }
}
