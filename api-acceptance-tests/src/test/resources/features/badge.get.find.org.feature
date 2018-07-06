@badge-get
Feature: Verify retrieve newly created badge

  Background:
    * url baseUrl

  Scenario: Verify findBadges
    * def badge =
    """
    {
    party: {
    typeCode: 'ORG',
    contact: {
    fullName: 'June Whitfield',
    buildingStreet: '65 Basil Chambers',
    line2: 'Northern Quarter',
    townCity: 'Manchester',
    postCode: 'OR6 8GG',
    primaryPhoneNumber: '01616548765',
    secondaryPhoneNumber: '01616548765',
    emailAddress: 'june@bigbrainknitting.com'
    },
    organisation: {
    badgeHolderName: 'ORGTEST1234'
    }
    },
    localAuthorityId: 187,
    localAuthorityRef: 'YOURCODE',
    applicationDate: '2018-04-23',
    applicationChannelCode: 'ONLINE',
    startDate: '2019-06-30',
    expiryDate: '2019-07-01',
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

    Given path 'badges'
    And param name = 'orgtest'
    When method GET
    Then status 200
    And match $.data[*].badgeNumber contains createdbadgeno

    Given path 'badges'
    And param postCode = 'OR68GG'
    When method GET
    Then status 200
    And match $.data[*].badgeNumber contains createdbadgeno