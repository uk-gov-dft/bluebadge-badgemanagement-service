package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import static uk.gov.dft.bluebadge.service.badgemanagement.service.ValidationKeyEnum.MISSING_ORG_OBJECT;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.ValidationKeyEnum.MISSING_PERSON_OBJECT;

import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Contact;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Organisation;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Party;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Person;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.exception.BadRequestException;

import java.time.LocalDate;

public class BadgeOrderRequestConverter implements ToEntityConverter<BadgeEntity, BadgeOrderRequest> {

  private static boolean isPerson(Party party) {
    return "PERSON".equals(party.getTypeCode());
  }

  @Override
  public BadgeEntity convertToEntity(BadgeOrderRequest model) {

    Contact contact = model.getParty().getContact();

    // Populate common data
    BadgeEntity badgeEntity =
        BadgeEntity.builder()
            .partyCode(model.getParty().getTypeCode())
            .badgeStatus(BadgeEntity.Status.NEW)
            .localAuthorityId(model.getLocalAuthorityId())
            .localAuthorityRef(model.getLocalAuthorityRef())
            .appDateTime(model.getApplicationDate())
            .appChannelCode(model.getApplicationChannelCode())
            .startDate(model.getStartDate())
            .expiryDate(model.getExpiryDate())
            .eligibilityCode(model.getEligibilityCode())
            .deliverToCode(model.getDeliverToCode())
            .deliverOptionCode(model.getDeliveryOptionCode())
            .contactName(contact.getFullName())
            .contactBuildingStreet(contact.getBuildingStreet())
            .contactLine2(contact.getLine2())
            .contactTownCity(contact.getTownCity())
            .contactEmailAddress(contact.getEmailAddress())
            .contactPostcode(ConvertUtils.formatPostcodeForEntity(contact.getPostCode()))
            .primaryPhoneNo(contact.getPrimaryPhoneNumber())
            .secondaryPhoneNo(contact.getSecondaryPhoneNumber())
            .numberOfBadges(null == model.getNumberOfBadges() ? 1 : model.getNumberOfBadges())
            .orderDate(LocalDate.now())
            .build();

    // Populate person/organisation specific data
    if (isPerson(model.getParty())) {
      Person person = model.getParty().getPerson();
      if (null == person) {
        throw new BadRequestException(MISSING_PERSON_OBJECT.getFieldErrorInstance());
      }
      badgeEntity.setHolderName(person.getBadgeHolderName());
      badgeEntity.setNino(person.getNino());
      badgeEntity.setDob(person.getDob());
      badgeEntity.setGenderCode(person.getGenderCode());
    } else {
      Organisation org = model.getParty().getOrganisation();
      if (null == org) {
        throw new BadRequestException(MISSING_ORG_OBJECT.getFieldErrorInstance());
      }
      badgeEntity.setHolderName(org.getBadgeHolderName());
    }

    return badgeEntity;
  }
}
