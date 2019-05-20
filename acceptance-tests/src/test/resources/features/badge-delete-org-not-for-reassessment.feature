@badge-delete
Feature: Verify delete a organisation badge with not-for-reassessment flag

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-3rd-party-scotland.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader

  Scenario: Verify wipe badge not-for-reassessment flag to NULL
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
    badgeHolderName: 'TestData ORGTEST1234'
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
    numberOfBadges: 1
    }
    """
    Given path 'badges'
    And request badge
    When method POST
    Then status 200
    And match $.data[*] contains "#notnull"
    * def badgeNo1 = $.data[0]

    Given path 'badges/' + badgeNo1
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    When method GET
    Then status 200

    Given path 'badges/' + badgeNo1
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    When method DELETE
    Then status 200

    Given path 'badges/' + badgeNo1
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    When method GET
    Then status 404

