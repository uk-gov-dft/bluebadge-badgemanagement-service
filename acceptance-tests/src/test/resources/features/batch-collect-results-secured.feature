@batch-collect-results-secured
Feature: Verify collect results secured

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify collect-badges 403 with third party token
    Given path 'badges/collect-batches'
    And request '{}'
    When method POST
    Then status 403



