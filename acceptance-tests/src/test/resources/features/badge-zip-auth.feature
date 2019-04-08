@badge-zip
Feature: Verify authentication of badge zip

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')

  Scenario: Verify fail when no auth
    * def result = callonce read('./oauth2-3rd-party-scotland.feature')
    Given path 'badges'
    And header Accept = 'application/vnd.bluebadge-api.v1+json, application/zip'
    And param laShortCode = 'ABERD'
    When method GET
    Then assert responseStatus == 401 || responseStatus == 406

  Scenario: Verify valid when 3rd party auth ok
    * def result = callonce read('./oauth2-3rd-party-scotland.feature')
    Given path 'badges'
    And header Authorization = 'Bearer ' + result.accessToken
    And header Accept = 'application/vnd.bluebadge-api.v1+json, application/zip'
    And param laShortCode = 'ABERD'
    When method GET
    Then status 200

  Scenario: Verify fail when 3rd party auth fails from different la
    * def result = callonce read('./oauth2-3rd-party-scotland.feature')
    Given path 'badges'
    And header Authorization = 'Bearer ' + result.accessToken
    And header Accept = 'application/vnd.bluebadge-api.v1+json, application/zip'
    And param laShortCode = 'ANGL'
    When method GET
    Then status 403

  Scenario: Verify fail when client auth
    * def result = callonce read('./oauth2-client.feature')
    Given path 'badges'
    And header Authorization = 'Bearer ' + result.accessToken
    And header Accept = 'application/vnd.bluebadge-api.v1+json, application/zip'
    And param laShortCode = 'ANGL'
    When method GET
    Then status 403

  Scenario: Confirm client auth was valid via different endpoint
    * def result = callonce read('./oauth2-client.feature')
    Given path 'reports/issued-badges'
    And header Authorization = 'Bearer ' + result.accessToken
    And param startDate = '2019-02-01'
    And param endDate = '2019-02-02'
    When method GET
    Then status 200

  Scenario: Verify fail when la editor auth
    * def result = callonce read('./oauth2-la-editor-scotland.feature')
    Given path 'badges'
    And header Authorization = 'Bearer ' + result.accessToken
    And header Accept = 'application/vnd.bluebadge-api.v1+json, application/zip'
    And param laShortCode = 'ABERD'
    When method GET
    Then status 403

  Scenario: Confirm editor auth was valid via different endpoint
    * def result = callonce read('./oauth2-la-editor-scotland.feature')
    Given path 'badges/EEEEEF/replacements'
    And header Authorization = 'Bearer ' + result.accessToken
    And request {badgeNumber: "EEEEEF","replaceReasonCode": "STOLE","deliverToCode": "HOME","deliveryOptionCode": "STAND"}
    When method POST
    Then status 200

  Scenario: Verify fail when dft admin auth
    * def result = callonce read('./oauth2-dft-admin.feature')
    Given path 'badges'
    And header Authorization = 'Bearer ' + result.accessToken
    And header Accept = 'application/vnd.bluebadge-api.v1+json, application/zip'
    And param laShortCode = 'ABERD'
    When method GET
    Then status 403

  Scenario: Verify fail when la read only auth
    * def result = callonce read('./oauth2-la-readonly-scotland.feature')
    Given path 'badges'
    And header Authorization = 'Bearer ' + result.accessToken
    And header Accept = 'application/zip'
    And param laShortCode = 'ABERD'
    When method GET
    Then status 403

  Scenario: Confirm la read only auth was valid via different endpoint
    * def result = callonce read('./oauth2-la-readonly-scotland.feature')
    Given path 'badges/EEEEEE'
    And header Authorization = 'Bearer ' + result.accessToken
    When method GET
    Then status 200

