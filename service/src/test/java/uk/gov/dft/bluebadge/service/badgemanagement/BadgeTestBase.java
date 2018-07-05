package uk.gov.dft.bluebadge.service.badgemanagement;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Contact;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Organisation;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Party;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Person;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.RefData;

public class BadgeTestBase {

  private boolean refDataAdded = false;

  private void addRefData() {
    if (refDataAdded) return;

    RefData.getValidGroupKeys()
        .addAll(
            Arrays.asList(
                "APPSOURCE_PAPER",
                "DELOP_STAND",
                "DELIVER_HOME",
                "ELIGIBILIT_PIP",
                "PARTY_PERSON",
                "GENDER_MALE"));

    refDataAdded = true;
  }

  protected BadgeEntity getValidBadge() {
    addRefData();
    return BadgeEntity.builder()
        .appChannelCode("PAPER")
        .appDateTime(LocalDate.now().minus(Period.ofDays(7)))
        .contactBuildingStreet("29 Listley Street")
        .contactLine2(null)
        .contactName("Robert McRoberts")
        .primaryPhoneNo("01234123456")
        .secondaryPhoneNo(null)
        .contactPostcode("WV16 4AW")
        .contactTownCity("Bridgnorth")
        .deliverOptionCode("STAND")
        .deliverToCode("HOME")
        .dob(LocalDate.now().minus(Period.ofYears(30)))
        .eligibilityCode("PIP")
        .expiryDate(LocalDate.now().plus(Period.ofYears(2)).plus(Period.ofMonths(1)))
        .genderCode("MALE")
        .holderName("Robert McRoberts")
        .localAuthorityId(2)
        .localAuthorityRef(null)
        .nino("NS123456A")
        .partyCode("PERSON")
        .startDate(LocalDate.now().plus(Period.ofMonths(1)))
        .badgeNo(null)
        .badgeStatus(null)
        .imageLink(null)
        .numberOfBadges(1)
        .build();
  }

  protected BadgeOrderRequest getValidBadgeOrderOrgRequest() {
    addRefData();
    BadgeOrderRequest request = new BadgeOrderRequest();
    Party party = new Party();
    Contact contact = new Contact();
    Organisation org = new Organisation();
    request.setApplicationChannelCode("PAPER");
    request.setApplicationDate(LocalDate.now().minus(Period.ofDays(2)));
    request.setDeliverToCode("HOME");
    request.setEligibilityCode("PIP");
    request.setDeliveryOptionCode("FAST");
    request.setLocalAuthorityId(2);
    request.setParty(party);
    request.setNumberOfBadges(1);
    party.setTypeCode("ORG");
    party.setOrganisation(org);
    org.setBadgeHolderName("An org");
    party.setContact(contact);
    contact.setBuildingStreet("12 The Rd");
    contact.setEmailAddress("a@b.com");
    contact.setFullName("contact name");
    contact.primaryPhoneNumber("12345678901");
    contact.setPostCode("WV16 4AW");
    contact.setTownCity("Bridgnorth");
    return request;
  }

  protected BadgeOrderRequest getValidBadgeOrderPersonRequest() {
    addRefData();
    BadgeOrderRequest request = new BadgeOrderRequest();
    Party party = new Party();
    Contact contact = new Contact();
    Person person = new Person();
    request.setApplicationChannelCode("PAPER");
    request.setApplicationDate(LocalDate.now().minus(Period.ofDays(2)));
    request.setDeliverToCode("HOME");
    request.setEligibilityCode("PIP");
    request.setDeliveryOptionCode("FAST");
    request.setLocalAuthorityId(2);
    request.setParty(party);
    request.setNumberOfBadges(1);
    party.setTypeCode("PERSON");
    party.setPerson(person);
    person.setBadgeHolderName("Peter Parker");
    person.setDob(LocalDate.now().minus(Period.ofYears(30)));
    person.setGenderCode("MALE");
    party.setContact(contact);
    contact.setBuildingStreet("12 The Rd");
    contact.setEmailAddress("a@b.com");
    contact.setFullName("contact name");
    contact.primaryPhoneNumber("12345678901");
    contact.setPostCode("WV16 4AW");
    contact.setTownCity("Bridgnorth");
    return request;
  }
}
