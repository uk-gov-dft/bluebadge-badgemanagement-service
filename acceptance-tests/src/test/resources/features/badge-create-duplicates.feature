@badge-create-org
Feature: Verify Cannot create duplicate badge

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Can't create when hash matches org but can when existing deleted
    * def badgeOrg =
    """
    {
    party: {
    typeCode: 'ORG',
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
    organisation: {
    badgeHolderName: 'TestData Org for dupe test.'
    }
    },
    localAuthorityShortCode: 'ABERD',
    localAuthorityRef: 'YOURCODE',
    applicationDate: '2018-04-23',
    applicationChannelCode: 'ONLINE',
    startDate: '#(futureDate)',
    expiryDate: '#(futureDatePlusYear)',
    eligibilityCode: 'CHILDBULK',
    imageFile: 'YWZpbGU=',
    deliverToCode: 'HOME',
    deliveryOptionCode: 'STAND',
    numberOfBadges: 2
    }
    """

    # Create a badge
    Given path 'badges'
    And request badgeOrg
    When method POST
    Then status 200
    And match $.data[*] contains "#notnull"
    * def badgeNoOrg1 = $.data[0]
    * def badgeNoOrg2 = $.data[1]

    # Try to create the same.
    Given path 'badges'
    * header Authorization = 'Bearer ' + result.accessToken
    And request badgeOrg
    When method POST
    Then status 400
    And match $.error contains {message: "AlreadyExists.badge"}

    # Set previously created badge to deleted
    Given path 'badges/' + badgeNoOrg1
    * header Authorization = 'Bearer ' + result.accessToken
    When method DELETE
    Then status 200
    Given path 'badges/' + badgeNoOrg2
    * header Authorization = 'Bearer ' + result.accessToken
    When method DELETE
    Then status 200

    # Can now create badge again
    Given path 'badges'
    * header Authorization = 'Bearer ' + result.accessToken
    And request badgeOrg
    When method POST
    Then status 200

  Scenario: Can't create when hash matches person but can when existing cancelled
    * def badgePerson =
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
    badgeHolderName: 'TestData Person duplicate',
    nino: 'NY 18 87 96 B',
    dob: '1972-09-12',
    genderCode: 'MALE'
    }
    },
    localAuthorityShortCode: 'ABERD',
    localAuthorityRef: 'YOURCODE',
    applicationDate: '2018-04-23',
    applicationChannelCode: 'ONLINE',
    startDate: '#(futureDate)',
    expiryDate: '#(futureDatePlusYear)',
    eligibilityCode: 'CHILDBULK',

    deliverToCode: 'HOME',
    deliveryOptionCode: 'STAND',
    numberOfBadges: 1
    }
    """

    # Create a badge
    Given path 'badges'
    And request badgePerson
    When method POST
    Then status 200
    And match $.data[*] contains "#notnull"
    * def badgeNoPerson = $.data[0]

    # Try to create the same.
    Given path 'badges'
    * header Authorization = 'Bearer ' + result.accessToken
    And request badgePerson
    When method POST
    Then status 400
    And match $.error contains {message: "AlreadyExists.badge"}

    # Set previously created badge to cancelled
    Given path 'badges/' + badgeNoPerson + '/cancellations'
    And request {badgeNumber: "#(badgeNoPerson)", cancelReasonCode: "NOLONG"}
    * header Authorization = 'Bearer ' + result.accessToken
    When method POST
    Then status 200

    # Can now create badge again
    Given path 'badges'
    * header Authorization = 'Bearer ' + result.accessToken
    And request badgePerson
    When method POST
    Then status 200

