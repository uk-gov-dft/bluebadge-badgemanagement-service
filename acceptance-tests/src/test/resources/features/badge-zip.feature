@badge-zip
Feature: Verify badge zip badge zip

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def ZipUtils = Java.type('uk.gov.service.bluebadge.test.utils.ZipUtils')
    * def db = new DbUtils(dbConfig)
    * def zipUtils = new ZipUtils()
    * def setup = callonce db.runScript('acceptance-test-data.sql')

  Scenario: Verify badge zip badge zip
    * def result = callonce read('./oauth2-3rd-party-scotland.feature')
    Given path 'badges'
    And header Authorization = 'Bearer ' + result.accessToken
    And header Accept = 'application/vnd.bluebadge-api.v1+json, application/zip'
    And param laShortCode = 'ABERD'
    When method GET
    Then status 200
    * assert zipUtils.isZip(responseBytes)