@badge-find-person
Feature: Verify find badge for newly created badge

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken
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

  Scenario: Verify findBadges
    Given path 'badges'
    And request badge
    When method POST
    Then status 200
    And match $.data[*] contains "#notnull"
    And def createdbadgeno = $.data[0]

  Scenario: Verify findBadges finds the created badge
    Given path 'badges'
    And param name = 'red'
    When method GET
    Then status 200
    And match $.data[*] contains "#notnull"
    And match $.data[*].badgeNumber contains createdbadgeno
    And match $.data[*].partyTypeDescription contains 'Person'

  Scenario: Verify findBadges finds nothing for unknown name
    Given path 'badges'
    And param name = 'IDONOTEXISTIWOULDHOPE'
    When method GET
    Then status 200
    And def stuff = $.data
    And assert stuff.length == 0

  Scenario: Verify findBadges bad request when missing name and postcode
    Given path 'badges'
    And param name = ''
    And param postCode = ''
    When method GET
    Then status 400
    And match $.error.message == 'NotNull.params.badge.find'

  Scenario: Verify findBadges bad request when name and postcode both provided
    Given path 'badges'
    And param name = 'CANTHAVENAMEANDPOSTCODE'
    And param postCode = 'WV164AW'
    When method GET
    Then status 400
    And match $.error.message == 'TooMany.params.badge.find'