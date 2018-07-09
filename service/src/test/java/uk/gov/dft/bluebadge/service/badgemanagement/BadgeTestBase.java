package uk.gov.dft.bluebadge.service.badgemanagement;

import static uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Contact;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Organisation;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Party;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Person;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.ReferenceDataService;

public class BadgeTestBase {

  public class DefaultVals {
    static final String ELIGIBILITY_CODE = "PIP";
    static final String DELIVER_TO_CODE = "HOME";
    static final String DELIVER_OPTION_CODE = "FAST";
    static final String APP_CHANNEL_CODE = "ONLINE";
    public static final String GENDER_CODE = "MALE";
    public static final String GENDER_DESC = "Male";
    static final String PARTY_PERSON_CODE = "PERSON";
    public static final String PARTY_PERSON_DESC = "Person";
    static final String PARTY_ORG_CODE = "ORG";
  }

  public BadgeTestBase() {
    addRefData();
  }

  private void addRefData() {

    ReferenceDataService.getValidGroupKeys()
        .addAll(
            Arrays.asList(
                APP_SOURCE.getGroupKey() + "_" + DefaultVals.APP_CHANNEL_CODE,
                DELIVERY_OPTIONS.getGroupKey() + "_" + DefaultVals.DELIVER_OPTION_CODE,
                DELIVER_TO.getGroupKey() + "_" + DefaultVals.DELIVER_TO_CODE,
                ELIGIBILITY.getGroupKey() + "_" + DefaultVals.ELIGIBILITY_CODE,
                PARTY.getGroupKey() + "_" + DefaultVals.PARTY_ORG_CODE,
                PARTY.getGroupKey() + "_" + DefaultVals.PARTY_PERSON_CODE,
                GENDER.getGroupKey() + "_" + DefaultVals.GENDER_CODE));

    ReferenceDataService.getKeyDescriptionMap()
        .put(GENDER.getGroupKey() + "_" + DefaultVals.GENDER_CODE, DefaultVals.GENDER_DESC);
    ReferenceDataService.getKeyDescriptionMap()
        .put(
            PARTY.getGroupKey() + "_" + DefaultVals.PARTY_PERSON_CODE,
            DefaultVals.PARTY_PERSON_DESC);
  }

  protected BadgeEntity getValidPersonBadgeEntity() {

    return BadgeEntity.builder()
        .appChannelCode(DefaultVals.APP_CHANNEL_CODE)
        .appDateTime(LocalDate.now().minus(Period.ofDays(7)))
        .contactBuildingStreet("29 Listley Street")
        .contactLine2(null)
        .contactName("Robert McRoberts")
        .primaryPhoneNo("01234123456")
        .secondaryPhoneNo(null)
        .contactPostcode("WV16 4AW")
        .contactTownCity("Bridgnorth")
        .deliverOptionCode(DefaultVals.DELIVER_OPTION_CODE)
        .deliverToCode(DefaultVals.DELIVER_TO_CODE)
        .dob(LocalDate.now().minus(Period.ofYears(30)))
        .eligibilityCode(DefaultVals.ELIGIBILITY_CODE)
        .expiryDate(LocalDate.now().plus(Period.ofYears(2)).plus(Period.ofMonths(1)))
        .genderCode(DefaultVals.GENDER_CODE)
        .holderName("Robert McRoberts")
        .localAuthorityId(2)
        .localAuthorityRef(null)
        .nino("NS123456A")
        .partyCode(DefaultVals.PARTY_PERSON_CODE)
        .startDate(LocalDate.now().plus(Period.ofMonths(1)))
        .badgeNo("KKKKKK")
        .badgeStatus(BadgeEntity.Status.NEW)
        .imageLink(null)
        .numberOfBadges(1)
        .orderDate(LocalDate.now())
        .build();
  }

  protected BadgeOrderRequest getValidBadgeOrderOrgRequest() {
    BadgeOrderRequest request = new BadgeOrderRequest();
    Party party = new Party();
    Contact contact = new Contact();
    Organisation org = new Organisation();
    request.setApplicationChannelCode(DefaultVals.APP_CHANNEL_CODE);
    request.setApplicationDate(LocalDate.now().minus(Period.ofDays(2)));
    request.setDeliverToCode(DefaultVals.DELIVER_TO_CODE);
    request.setEligibilityCode(DefaultVals.ELIGIBILITY_CODE);
    request.setDeliveryOptionCode(DefaultVals.DELIVER_OPTION_CODE);
    request.setLocalAuthorityId(2);
    request.setParty(party);
    request.setNumberOfBadges(1);
    party.setTypeCode(DefaultVals.PARTY_ORG_CODE);
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
    BadgeOrderRequest request = new BadgeOrderRequest();
    Party party = new Party();
    Contact contact = new Contact();
    Person person = new Person();
    request.setApplicationChannelCode(DefaultVals.APP_CHANNEL_CODE);
    request.setApplicationDate(LocalDate.now().minus(Period.ofDays(2)));
    request.setDeliverToCode(DefaultVals.DELIVER_TO_CODE);
    request.setEligibilityCode(DefaultVals.ELIGIBILITY_CODE);
    request.setDeliveryOptionCode(DefaultVals.DELIVER_OPTION_CODE);
    request.setLocalAuthorityId(2);
    request.setParty(party);
    request.setNumberOfBadges(1);
    party.setTypeCode(DefaultVals.PARTY_PERSON_CODE);
    party.setPerson(person);
    person.setBadgeHolderName("Peter Parker");
    person.setDob(LocalDate.now().minus(Period.ofYears(30)));
    person.setGenderCode(DefaultVals.GENDER_CODE);
    party.setContact(contact);
    contact.setBuildingStreet("12 The Rd");
    contact.setEmailAddress("a@b.com");
    contact.setFullName("contact name");
    contact.primaryPhoneNumber("12345678901");
    contact.setPostCode("WV16 4AW");
    contact.setTownCity("Bridgnorth");
    return request;
  }

  protected BadgeEntity getValidOrgBadgeEntity() {
    return BadgeEntity.builder()
        .contactPostcode("WV164AW")
        .contactTownCity("Bridgnorth")
        .contactLine2(null)
        .contactBuildingStreet("21 Listley St")
        .contactName("Harry Kane")
        .genderCode(null)
        .dob(null)
        .nino(null)
        .holderName("Harry Kane")
        .deliverOptionCode(DefaultVals.DELIVER_OPTION_CODE)
        .deliverToCode(DefaultVals.DELIVER_TO_CODE)
        .imageLink(null)
        .eligibilityCode(DefaultVals.ELIGIBILITY_CODE)
        .expiryDate(LocalDate.now().plus(Period.ofMonths(35)))
        .startDate(LocalDate.now().plus(Period.ofMonths(1)))
        .appChannelCode(DefaultVals.APP_CHANNEL_CODE)
        .appDateTime(LocalDate.now().minus(Period.ofDays(1)))
        .localAuthorityRef("LA_REF")
        .localAuthorityId(2)
        .partyCode(DefaultVals.PARTY_ORG_CODE)
        .badgeNo("KKKKKK")
        .badgeStatus(BadgeEntity.Status.NEW)
        .numberOfBadges(1)
        .primaryPhoneNo("01234512312")
        .secondaryPhoneNo(null)
        .contactEmailAddress("a@b.c")
        .build();
  }
}
