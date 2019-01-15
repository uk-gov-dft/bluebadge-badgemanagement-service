@badge-print-batch
Feature: Verify print a batch

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-print-batch.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify print a badge - valid batch type (FASTTRACK)
    Given path 'badges/print-batch'
    And request {"batchType": "FASTTRACK"}
    When method POST
    Then status 200

  Scenario: Verify print a badge - valid batch type (STANDARD)
    Given path 'badges/print-batch'
    And request {"batchType": "STANDARD"}
    When method POST
    Then status 200

  Scenario: Verify print a badge - valid batch type (LA)
    Given path 'badges/print-batch'
    And request {"batchType": "LA"}
    When method POST
    Then status 200

  Scenario: Verify print a badge - invalid batch type
    Given path 'badges/print-batch'
    And request {"batchType": "INVALID"}
    When method POST
    Then status 400
    And match $.error.message == 'InvalidFormat.BatchType'

  Scenario: Verify print a badge - missing batch type
    Given path 'badges/print-batch'
    And request {"batchType": ""}
    When method POST
    Then status 400




