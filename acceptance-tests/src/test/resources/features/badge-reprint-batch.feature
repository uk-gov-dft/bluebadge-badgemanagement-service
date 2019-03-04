@badge-reprint-batch
Feature: Verify reprint a batch

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def setup = callonce db.runScript('acceptance-test-batch-data.sql')
    * def result = callonce read('./oauth2-print-batch.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify reprint a badge - valid batchId

      # Creates a print batch
    Given path 'badges/print-batch'
    And request {"batchType": "STANDARD"}
    When method POST
    Then status 200

    * def result = callonce read('./oauth2-print-batch.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * def batchCountBefore = db.countBatches()
    * def lastBatchId = db.getLastBatchId()
    Given path 'badges/print-batch/' + lastBatchId
    And request {}
    When method POST
    Then status 200
    # And NO batch is created
    * assert batchCountBefore == db.countBatches()

  Scenario: Verify reprint a badge - invalid batchId
    Given path 'badges/print-batch/-999'
    And request {}
    When method POST
    Then status 400

  Scenario: Verify reprint a badge - valid batchId but doesn't exist
    Given path 'badges/print-batch/31415978'
    And request {}
    When method POST
    Then status 404