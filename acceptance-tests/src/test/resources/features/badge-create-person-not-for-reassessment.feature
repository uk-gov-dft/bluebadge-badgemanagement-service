@badge-create-not-for-reassessment
Feature: Verify create badge with/without not-for-reassessment flag
  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-3rd-party-scotland.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    * def testData = read('classpath:test-data.json')

  Scenario: Verify valid create non-automatic eligible person badge with TRUE value
    * set testData.personBadge.notForReassessment = true
    Given path 'badges'
    And request testData.personBadge
    When method POST
    Then status 200
    And match $.data[*] contains "#notnull"
    * def badgeNo1 = $.data[0]

    Given path 'badges/' + badgeNo1
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    When method GET
    Then status 200
    And def result = $.data
    And match result.badgeNumber == badgeNo1
    And match result.notForReassessment == true

  Scenario: Verify valid create non-automatic eligible person badge with FALSE value
    * set testData.personBadge.party.person.nino = 'XY188796C'
    * set testData.personBadge.notForReassessment = false
    * print 'Modified:', testData.personBadge
    Given path 'badges'
    And request testData.personBadge
    When method POST
    Then status 200
    And match $.data[*] contains "#notnull"
    * def badgeNo = $.data[0]

    Given path 'badges/' + badgeNo
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    When method GET
    Then status 200
    And def result = $.data
    And match result.badgeNumber == badgeNo
    And match result.notForReassessment == false

  Scenario: Verify valid create non-automatic person badge without the flag and flag is automatically set to FALSE
    * set testData.personBadge.party.person.nino = 'HN188796D'
    Given path 'badges'
    And request testData.personBadge
    When method POST
    Then status 200
    And match $.data[*] contains "#notnull"
    * def badgeNo = $.data[0]

    Given path 'badges/' + badgeNo
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    When method GET
    Then status 200
    And def result = $.data
    And match result.badgeNumber == badgeNo
    And match result.notForReassessment == false

  Scenario: Verify invalid create automatic person badge with the flag being TRUE
    * set testData.personBadge.eligibilityCode = 'BLIND'
    * set testData.personBadge.notForReassessment = true
    Given path 'badges'
    And request testData.personBadge
    When method POST
    Then status 400
    And match $.error.errors contains {field:"notForReassessment", reason:"Not for reassessment not accepted for automatic eligibility criteria.", message:"Person.invalid.badge.notForReassessment","location":null,"locationType":null}}

  Scenario: Verify invalid create automatic person badge with the flag being FALSE
    * set testData.personBadge.eligibilityCode = 'PIP'
    * set testData.personBadge.notForReassessment = false
    Given path 'badges'
    And request testData.personBadge
    When method POST
    Then status 400
    And match $.error.errors contains {field:"notForReassessment", reason:"Not for reassessment not accepted for automatic eligibility criteria.", message:"Person.invalid.badge.notForReassessment","location":null,"locationType":null}}

  Scenario: Verify create automatic eligible person badge without the flag and it will remain unset after that
    * set testData.personBadge.party.person.nino = 'AB188796C'
    * set testData.personBadge.eligibilityCode = 'WPMS'
    Given path 'badges'
    And request testData.personBadge
    When method POST
    Then status 200
    And match $.data[*] contains "#notnull"
    * def badgeNo = $.data[0]

    Given path 'badges/' + badgeNo
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    When method GET
    Then status 200
    And def result = $.data
    And match result.badgeNumber == badgeNo
    And match result.notForReassessment == null
