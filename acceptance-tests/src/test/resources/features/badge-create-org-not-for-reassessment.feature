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

  Scenario: Verify invalid create organisation badge with the flag being TRUE
    * set testData.orgBadge.notForReassessment = true
    Given path 'badges'
    And request testData.orgBadge
    When method POST
    Then status 400
    And match $.error.errors contains {field:"notForReassessment", reason:"Not for reassessment not accepted for organisations.", message:"Organisation.invalid.badge.notForReassessment","location":null,"locationType":null}}

  Scenario: Verify invalid create organisation badge with the flag being FALSE
    * set testData.orgBadge.notForReassessment = true
    Given path 'badges'
    And request testData.orgBadge
    When method POST
    Then status 400
    And match $.error.errors contains {field:"notForReassessment", reason:"Not for reassessment not accepted for organisations.", message:"Organisation.invalid.badge.notForReassessment","location":null,"locationType":null}}

  Scenario: Verify create organisation badge without the flag and it will remain unset after that
    Given path 'badges'
    And request testData.orgBadge
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