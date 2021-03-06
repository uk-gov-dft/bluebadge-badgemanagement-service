@badge-create-person-traffic-risk
Feature: Verify Create badge traffic risk

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
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
    badgeHolderName: 'TestData Fred Cognitive',
    nino: 'NY 18 87 96 B',
    dob: '1972-09-12',
    genderCode: 'MALE'
    }
    },
    localAuthorityRef: 'YOURCODE',
    applicationDate: '2018-04-23',
    applicationChannelCode: 'ONLINE',
    startDate: '#(futureDate)',
    expiryDate: '#(futureDatePlusYear)',
    eligibilityCode: 'TRAF_RISK',
    deliverToCode: 'HOME',
    deliveryOptionCode: 'STAND',
    numberOfBadges: 1
    }
    """

  Scenario: Verify valid create traffic risk
    * def result = callonce read('./oauth2-3rd-party-scotland.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    * set badge.localAuthorityShortCode = "ABERD"
    Given path 'badges'
    And request badge
    When method POST
    Then status 200
    And match $.data[*] contains "#notnull"

  Scenario: Verify cannot create traffic risk if Welsh LA
    * def result = callonce read('./oauth2-3rd-party-wales.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    * set badge.localAuthorityShortCode = "ANGL"
    Given path 'badges'
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    And request badge
    When method POST
    Then status 400
    And match $.error.errors[0].message == "InvalidNation.badge.eligibilityCode"

  Scenario: Verify cannot create traffic risk if English LA
    * def result = callonce read('./oauth2-3rd-party-england.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    * set badge.localAuthorityShortCode = "BIRM"
    Given path 'badges'
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    And request badge
    When method POST
    Then status 400
    And match $.error.errors[0].message == "InvalidNation.badge.eligibilityCode"

  Scenario: Verify cannot create traffic risk if N Irish LA
    * def result = callonce read('./oauth2-3rd-party-nireland.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    * set badge.localAuthorityShortCode = "NIRE"
    Given path 'badges'
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    And request badge
    When method POST
    Then status 400
    And match $.error.errors[0].message == "InvalidNation.badge.eligibilityCode"