@badge-create-business-rule-bad
Feature: Verify Create badge with 400

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify create 400 with business validation start in past
    * def badge =
    """
      {
        party: {
          typeCode: 'PERSON',
          contact: {
            fullName: 'June Whitfield',
            buildingStreet: '65 Basil Chambers',
            line2: 'Northern Quarter',
            townCity: 'Manchester',
            postCode: 'WV164AW',
            primaryPhoneNumber: '01616548765',
            secondaryPhoneNumber: '01616548765',
            emailAddress: 'june@bigbrainknitting.com'
          },
          person: {
            badgeHolderName: 'TestData Bloggs',
            nino: 'NY188796B',
            dob: '1972-09-12',
            genderCode: 'MALE'
          },
          organisation: {
            badgeHolderName: 'The Monroe Institute'
          }
        },
        localAuthorityShortCode: 'ABERD',
        localAuthorityRef: 'YOURCODE',
        applicationDate: '2018-04-23',
        applicationChannelCode: 'ONLINE',
        startDate: '2016-06-30',
        expiryDate: '2017-07-01',
        eligibilityCode: 'CHILDBULK',
        imageFile: 'YWZpbGU=',
        deliverToCode: 'HOME',
        deliveryOptionCode: 'STAND',
        numberOfBadges: 1
      }
    """
    Given path 'badges'
    And request badge
    When method POST
    Then status 400
    And match $.error.errors contains {field:"startDate", reason:"#notnull", message:"#notnull", location:"#null", locationType:"#null"}

  Scenario: Verify create 400 when fast delivery option selected with council as delivery to
    * def badgeFastToCouncil =
    """
      {
        party: {
          typeCode: 'PERSON',
          contact: {
            fullName: 'June Whitfield',
            buildingStreet: '65 Basil Chambers',
            line2: 'Northern Quarter',
            townCity: 'Manchester',
            postCode: 'WV164AW',
            primaryPhoneNumber: '01616548765',
            secondaryPhoneNumber: '01616548765',
            emailAddress: 'june@bigbrainknitting.com'
          },
          person: {
            badgeHolderName: 'TestData Bloggs',
            nino: 'NY188796B',
            dob: '1972-09-12',
            genderCode: 'MALE'
          },
          organisation: {
            badgeHolderName: 'The Monroe Institute'
          }
        },
        localAuthorityShortCode: 'ABERD',
        localAuthorityRef: 'YOURCODE',
        applicationDate: '2018-04-23',
        applicationChannelCode: 'ONLINE',
        startDate: '2026-06-30',
        expiryDate: '2027-07-01',
        eligibilityCode: 'CHILDBULK',
        imageFile: 'YWZpbGU=',
        deliverToCode: 'COUNCIL',
        deliveryOptionCode: 'FAST',
        numberOfBadges: 1
      }
    """
    Given path 'badges'
    And request badgeFastToCouncil
    When method POST
    Then status 400
    And match $.error.errors contains {field:"deliverToCode", reason:"Only 'standard' delivery option is available when delivering to council.", message:"Invalid.badge.deliverOptionCode", location:"#null", locationType:"#null"}

  Scenario: Verify create 400 when address field character limits are exceeded
    * def badgeAddressFieldCharacterLimitsExceeded =
    """
      {
        party: {
          typeCode: 'PERSON',
          contact: {
            fullName: 'June Whitfield',
            buildingStreet: 'I am pretty sure this string is way over fifty characters',
            line2: 'This string is just over forty characters',
            townCity: 'This string is just over forty characters',
            postCode: 'WV164AW',
            primaryPhoneNumber: '01616548765',
            secondaryPhoneNumber: '01616548765',
            emailAddress: 'june@bigbrainknitting.com'
          },
          person: {
            badgeHolderName: 'TestData Bloggs',
            nino: 'NY188796B',
            dob: '1972-09-12',
            genderCode: 'MALE'
          },
          organisation: {
            badgeHolderName: 'The Monroe Institute'
          }
        },
        localAuthorityShortCode: 'ABERD',
        localAuthorityRef: 'YOURCODE',
        applicationDate: '2018-04-23',
        applicationChannelCode: 'ONLINE',
        startDate: '2026-06-30',
        expiryDate: '2027-07-01',
        eligibilityCode: 'CHILDBULK',
        imageFile: 'YWZpbGU=',
        deliverToCode: 'HOME',
        deliveryOptionCode: 'STAND',
        numberOfBadges: 1
      }
    """
    Given path 'badges'
    And request badgeAddressFieldCharacterLimitsExceeded
    When method POST
    Then status 400
    And match $.error.errors contains {"field":"party.contact.townCity","reason":"size must be between 0 and 40","message":"Size.badgeOrderRequest.party.contact.townCity","location":null,"locationType":null},{"field":"party.contact.buildingStreet","reason":"size must be between 0 and 50","message":"Size.badgeOrderRequest.party.contact.buildingStreet","location":null,"locationType":null},{"field":"party.contact.line2","reason":"size must be between 0 and 40","message":"Size.badgeOrderRequest.party.contact.line2","location":null,"locationType":null}

  Scenario: Verify create 400 when application date is set in the future
    * def badgeFutureApplicationDate =
    """
      {
        party: {
          typeCode: 'PERSON',
          contact: {
            fullName: 'June Whitfield',
            buildingStreet: '65 Basil Chambers',
            line2: 'Northern Quarter',
            townCity: 'Manchester',
            postCode: 'WV164AW',
            primaryPhoneNumber: '01616548765',
            secondaryPhoneNumber: '01616548765',
            emailAddress: 'june@bigbrainknitting.com'
          },
          person: {
            badgeHolderName: 'TestData Bloggs',
            nino: 'NY188796B',
            dob: '1972-09-12',
            genderCode: 'MALE'
          },
          organisation: {
            badgeHolderName: 'The Monroe Institute'
          }
        },
        localAuthorityShortCode: 'ABERD',
        localAuthorityRef: 'YOURCODE',
        applicationDate: '#(futureDatePlusYear)',
        applicationChannelCode: 'ONLINE',
        startDate: '2026-06-30',
        expiryDate: '2027-07-01',
        eligibilityCode: 'CHILDBULK',
        imageFile: 'YWZpbGU=',
        deliverToCode: 'HOME',
        deliveryOptionCode: 'STAND',
        numberOfBadges: 1
      }
    """
    Given path 'badges'
    And request badgeFutureApplicationDate
    When method POST
    Then status 400
    And match $.error.errors contains {"field":"applicationDate","reason":"Application date must be in the past.","message":"DateInPast.badge.applicationDate","location":null,"locationType":null}
    