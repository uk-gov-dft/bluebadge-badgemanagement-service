package uk.gov.dft.bluebadge.service.badgemanagement;

import uk.gov.dft.bluebadge.model.badgemanagement.generated.BadgeOrderRequest;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Contact;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Organisation;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Party;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.Person;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.ReferenceDataApiClient;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model.Nation;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model.ReferenceData;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum.APP_SOURCE;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum.DELIVERY_OPTIONS;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum.DELIVER_TO;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum.ELIGIBILITY;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum.GENDER;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum.PARTY;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum.REPLACE;

public class BadgeTestFixture {

  public class DefaultVals {
    static final String LOCAL_AUTHORITY_CODE_ENGLAND = "BIRM";
    static final String LOCAL_AUTHORITY_CODE_WALES = "CARDIFF";
    static final String LOCAL_AUTHORITY_CODE_N_IRELAND = "BELFAST";
    static final String LOCAL_AUTHORITY_CODE_SCOTLAND = "ABERD";
    static final String ELIGIBILITY_CODE = "PIP";
    static final String DELIVER_TO_CODE_HOME = "HOME";
    static final String DELIVER_TO_CODE_COUNCIL = "COUNCIL";
    static final String DELIVER_OPTION_CODE_FAST = "FAST";
    static final String DELIVER_OPTION_CODE_STAND = "STAND";
    static final String APP_CHANNEL_CODE = "ONLINE";
    public static final String GENDER_CODE = "MALE";
    static final String GENDER_DESC = "Male";
    static final String PARTY_PERSON_CODE = "PERSON";
    static final String PARTY_PERSON_DESC = "Person";
    static final String PARTY_ORG_CODE = "ORG";
    public static final String CANCEL_CODE_VALID = "NOLONG";
    public static final String REPLACE_REASON = "STOLE";
  }

  private BadgeTestFixture() {}

  public static ReferenceDataApiClient getMockRefDataApiClient() {
    ReferenceDataApiClient client = mock(ReferenceDataApiClient.class);
    when(client.retrieveReferenceData()).thenReturn(getRefDataTestList());
    return client;
  }

  private static ReferenceData getNewRefDataItem(
      RefDataGroupEnum group, String key, String description) {
    ReferenceData data = new ReferenceData();
    data.setGroupShortCode(group.getGroupKey());
    data.setShortCode(key);
    data.setDescription(description);
    data.setDisplayOrder(1);
    return data;
  }

  private static LocalAuthorityRefData getNewLaRefDataItem(String key, Nation nation) {
    LocalAuthorityRefData data = new LocalAuthorityRefData();
    data.setGroupShortCode(RefDataGroupEnum.LA.getGroupKey());
    data.setShortCode(key);
    data.setDisplayOrder(1);
    LocalAuthorityRefData.LocalAuthorityMetaData meta =
        new LocalAuthorityRefData.LocalAuthorityMetaData();
    meta.setNation(nation);
    data.setLocalAuthorityMetaData(meta);

    return data;
  }

  private static List<ReferenceData> getRefDataTestList() {
    List<ReferenceData> referenceDataList = new ArrayList<>();
    referenceDataList.add(getNewRefDataItem(APP_SOURCE, DefaultVals.APP_CHANNEL_CODE, null));
    referenceDataList.add(
        getNewRefDataItem(DELIVERY_OPTIONS, DefaultVals.DELIVER_OPTION_CODE_FAST, null));
    referenceDataList.add(
        getNewRefDataItem(DELIVERY_OPTIONS, DefaultVals.DELIVER_OPTION_CODE_STAND, null));
    referenceDataList.add(getNewRefDataItem(DELIVER_TO, DefaultVals.DELIVER_TO_CODE_HOME, null));
    referenceDataList.add(getNewRefDataItem(DELIVER_TO, DefaultVals.DELIVER_TO_CODE_COUNCIL, null));
    referenceDataList.add(getNewRefDataItem(ELIGIBILITY, DefaultVals.ELIGIBILITY_CODE, null));
    referenceDataList.add(
        getNewLaRefDataItem(DefaultVals.LOCAL_AUTHORITY_CODE_SCOTLAND, Nation.SCO));
    referenceDataList.add(
        getNewLaRefDataItem(DefaultVals.LOCAL_AUTHORITY_CODE_ENGLAND, Nation.ENG));
    referenceDataList.add(getNewLaRefDataItem(DefaultVals.LOCAL_AUTHORITY_CODE_WALES, Nation.WLS));
    referenceDataList.add(
        getNewLaRefDataItem(DefaultVals.LOCAL_AUTHORITY_CODE_N_IRELAND, Nation.NIR));

    referenceDataList.add(getNewRefDataItem(PARTY, DefaultVals.PARTY_ORG_CODE, null));
    referenceDataList.add(
        getNewRefDataItem(PARTY, DefaultVals.PARTY_PERSON_CODE, DefaultVals.PARTY_PERSON_DESC));
    referenceDataList.add(
        getNewRefDataItem(GENDER, DefaultVals.GENDER_CODE, DefaultVals.GENDER_DESC));
    referenceDataList.add(
        getNewRefDataItem(
            RefDataGroupEnum.CANCEL, DefaultVals.CANCEL_CODE_VALID, "No longer needed."));
    referenceDataList.add(getNewRefDataItem(REPLACE, DefaultVals.REPLACE_REASON, null));
    return referenceDataList;
  }

  public static BadgeEntity getValidPersonBadgeEntity() {

    return BadgeEntity.builder()
        .appChannelCode(DefaultVals.APP_CHANNEL_CODE)
        .appDate(LocalDate.now().minus(Period.ofDays(7)))
        .contactBuildingStreet("29 Listley Street")
        .contactLine2(null)
        .contactName("Robert McRoberts")
        .primaryPhoneNo("01234123456")
        .secondaryPhoneNo(null)
        .contactPostcode("WV16 4AW")
        .contactTownCity("Bridgnorth")
        .deliverOptionCode(DefaultVals.DELIVER_OPTION_CODE_FAST)
        .deliverToCode(DefaultVals.DELIVER_TO_CODE_HOME)
        .dob(LocalDate.now().minus(Period.ofYears(30)))
        .eligibilityCode(DefaultVals.ELIGIBILITY_CODE)
        .expiryDate(LocalDate.now().plus(Period.ofYears(2)).plus(Period.ofMonths(1)))
        .genderCode(DefaultVals.GENDER_CODE)
        .holderName("Robert McRoberts")
        .localAuthorityShortCode("ABERD")
        .localAuthorityRef(null)
        .nino("NS123456A")
        .partyCode(DefaultVals.PARTY_PERSON_CODE)
        .startDate(LocalDate.now().plus(Period.ofMonths(1)))
        .badgeNo("KKKKKK")
        .badgeStatus(BadgeEntity.Status.ISSUED)
        .imageLink(null)
        .numberOfBadges(1)
        .orderDate(LocalDate.now())
        .replaceReasonCode(DefaultVals.REPLACE_REASON)
        .build();
  }

  public static BadgeOrderRequest getValidBadgeOrderOrgRequest() {
    BadgeOrderRequest request = new BadgeOrderRequest();
    Party party = new Party();
    Contact contact = new Contact();
    Organisation org = new Organisation();
    request.setApplicationChannelCode(DefaultVals.APP_CHANNEL_CODE);
    request.setApplicationDate(LocalDate.now().minus(Period.ofDays(2)));
    request.setDeliverToCode(DefaultVals.DELIVER_TO_CODE_HOME);
    request.setEligibilityCode(DefaultVals.ELIGIBILITY_CODE);
    request.setDeliveryOptionCode(DefaultVals.DELIVER_OPTION_CODE_FAST);
    request.setLocalAuthorityShortCode("ABERD");
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

  public static BadgeOrderRequest getValidBadgeOrderPersonRequest() {
    BadgeOrderRequest request = new BadgeOrderRequest();
    Party party = new Party();
    Contact contact = new Contact();
    Person person = new Person();
    request.setApplicationChannelCode(DefaultVals.APP_CHANNEL_CODE);
    request.setApplicationDate(LocalDate.now().minus(Period.ofDays(2)));
    request.setDeliverToCode(DefaultVals.DELIVER_TO_CODE_HOME);
    request.setEligibilityCode(DefaultVals.ELIGIBILITY_CODE);
    request.setDeliveryOptionCode(DefaultVals.DELIVER_OPTION_CODE_FAST);
    request.setLocalAuthorityShortCode("ABERD");
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

  public static BadgeEntity getValidOrgBadgeEntity() {
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
        .deliverOptionCode(DefaultVals.DELIVER_OPTION_CODE_FAST)
        .deliverToCode(DefaultVals.DELIVER_TO_CODE_HOME)
        .imageLink(null)
        .eligibilityCode(DefaultVals.ELIGIBILITY_CODE)
        .expiryDate(LocalDate.now().plus(Period.ofMonths(35)))
        .startDate(LocalDate.now().plus(Period.ofMonths(1)))
        .appChannelCode(DefaultVals.APP_CHANNEL_CODE)
        .appDate(LocalDate.now().minus(Period.ofDays(1)))
        .localAuthorityRef("LA_REF")
        .localAuthorityShortCode("ABERD")
        .partyCode(DefaultVals.PARTY_ORG_CODE)
        .badgeNo("KKKKKK")
        .badgeStatus(BadgeEntity.Status.ISSUED)
        .numberOfBadges(1)
        .primaryPhoneNo("01234512312")
        .secondaryPhoneNo(null)
        .contactEmailAddress("a@b.c")
        .build();
  }
}
