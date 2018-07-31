@badge-post
Feature: Verify Create badge

  Background:
    * url baseUrl

  Scenario: Verify valid create mixed case postcode
    * def badge =
    """
    {
    party: {
    typeCode: 'PERSON',
    contact: {
    fullName: '',
    buildingStreet: '65 Basil Chambers',
    line2: 'Northern Quarter',
    townCity: 'Manchester',
    postCode: 'wK6 8GH',
    primaryPhoneNumber: '01616548765',
    secondaryPhoneNumber: '01616548765',
    emailAddress: 'june@bigbrainknitting.com'
    },
    person: {
    badgeHolderName: 'Fred Bloggs',
    nino: 'NY188796B',
    dob: '1972-09-12',
    genderCode: 'MALE'
    },
    organisation: {
    badgeHolderName: 'The Monroe Institute'
    }
    },
    localAuthorityId: 187,
    localAuthorityRef: 'YOURCODE',
    applicationDate: '2018-04-23',
    applicationChannelCode: 'ONLINE',
    startDate: '#(futureDate)',
    expiryDate: '#(futureDatePlusYear)',
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
    Then status 200
    And match $.data[*] contains "#notnull"
    And def createdbadgeno = $.data[0]

    Given path 'badges/' + createdbadgeno
    When method GET
    Then status 200
    And def result = $.data
    And match result.badgeNumber == createdbadgeno
    And match result.localAuthorityId == 2
