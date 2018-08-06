package uk.gov.dft.bluebadge.service.badgemanagement;

import static org.mockito.Mockito.when;
import static uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.model.badgemanagement.generated.*;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.ReferenceDataApiClient;
import uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice.model.ReferenceData;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.service.referencedata.RefDataGroupEnum;
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
    public static final String CANCEL_CODE_VALID = "NOLONG";
    public static final String LOCAL_AUTHORITY_CODE = "ABERD";
  }

  protected ReferenceDataService referenceDataService;

  @Mock private ReferenceDataApiClient referenceDataApiClient;

  public BadgeTestBase() {
    MockitoAnnotations.initMocks(this);
    addRefData();
  }

  private ReferenceData getNewRefDataItem(RefDataGroupEnum group, String key, String description) {
    return ReferenceData.builder()
        .groupShortCode(group.getGroupKey())
        .shortCode(key)
        .description(description)
        .displayOrder(1)
        .groupDescription(null)
        .subgroupDescription(null)
        .subgroupShortCode(null)
        .build();
  }

  private void addRefData() {
    List<ReferenceData> referenceDataList = new ArrayList<>();
    referenceDataList.add(getNewRefDataItem(APP_SOURCE, DefaultVals.APP_CHANNEL_CODE, null));
    referenceDataList.add(
        getNewRefDataItem(DELIVERY_OPTIONS, DefaultVals.DELIVER_OPTION_CODE, null));
    referenceDataList.add(getNewRefDataItem(DELIVER_TO, DefaultVals.DELIVER_TO_CODE, null));
    referenceDataList.add(getNewRefDataItem(ELIGIBILITY, DefaultVals.ELIGIBILITY_CODE, null));
    referenceDataList.add(getNewRefDataItem(LA, DefaultVals.LOCAL_AUTHORITY_CODE, null));
    referenceDataList.add(getNewRefDataItem(PARTY, DefaultVals.PARTY_ORG_CODE, null));
    referenceDataList.add(
        getNewRefDataItem(PARTY, DefaultVals.PARTY_PERSON_CODE, DefaultVals.PARTY_PERSON_DESC));
    referenceDataList.add(
        getNewRefDataItem(GENDER, DefaultVals.GENDER_CODE, DefaultVals.GENDER_DESC));
    referenceDataList.add(
        getNewRefDataItem(
            RefDataGroupEnum.CANCEL, DefaultVals.CANCEL_CODE_VALID, "No longer needed."));

    when(referenceDataApiClient.retrieveReferenceData()).thenReturn(referenceDataList);
    referenceDataService = new ReferenceDataService(referenceDataApiClient);
    referenceDataService.groupContainsKey(RefDataGroupEnum.GENDER, "MALE");
  }

  protected BadgeEntity getValidPersonBadgeEntity() {

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
        .deliverOptionCode(DefaultVals.DELIVER_OPTION_CODE)
        .deliverToCode(DefaultVals.DELIVER_TO_CODE)
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
