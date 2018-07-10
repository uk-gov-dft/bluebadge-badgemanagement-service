@badge-get
Feature: Verify badge details

  Background:
    * url baseUrl

  Scenario: Verify retrieve a badge
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
    badgeHolderName: 'ORGTEST12345'
    }
    },
    localAuthorityId: 187,
    localAuthorityRef: 'YOURCODE',
    applicationDate: '2018-04-23T14:00:00Z',
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

    Given path 'badges/' + createdbadgeno
    When method GET
    Then status 200
    And match $.data.party.contact.fullName contains 'June Whitfield'
    And def apptime = $.data

    * print '=============================' + apptime

    Given  path 'badges/ZZZZZZ'
    When method GET
    Then status 404
    And match $.error.message contains 'NotFound.badge'