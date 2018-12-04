@badge-print-batch
Feature: Verify print a batch

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken
#    * def createResult = callonce read('./badge-create-person.feature')
# CREATE SEVERAL BADGES WITH FASTTRACK, STANDARD, LA with different STATUSES
#    * def createdBadgeNo = createResult.badgeNo

#  Scenario: Verify print a badge - valid batch type (FASTTRACK)
#    Given path 'badges/print-batch'
#    And request {"badgeType": "FASTTRACK"}
#    When method POST
#    Then status 200
##Match output $.
## match foo == '#[2]' para checar la size
#
#  Scenario: Verify print a badge - valid batch type (STANDARD)
#    Given path 'badges/print-batch'
#    And request {"batchType": "STANDARD"}
#    When method POST
#    Then status 200
##Match output $.
#
#  Scenario: Verify print a badge - valid batch type (LA)
#    Given path 'badges/print-batch'
#    And request {"batchType": "LA"}
#    When method POST
#    Then status 200
##Match output $.

  Scenario: Verify print a badge - invalid batch type
    Given path 'badges/print-batch'
    And request {"batchType": "INVALID"}
    When method POST
    Then status 400
#Match output $.error.errors[0].message

  Scenario: Verify print a badge - missing batch type
    Given path 'badges/print-batch'
    And request {"batchType": ""}
    When method POST
    Then status 400
#Match output $.error.errors[0].message

