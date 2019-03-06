@badge-print-batch
Feature: Verify print a batch

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-client.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify issued badges report
    Given path 'reports/issued-badges'
    And param startDate = '2019-02-01'
    And param endDate = '2019-02-02'
    When method GET
    Then status 200

  Scenario: Verify issued badges report - missing start date
    Given path 'reports/issued-badges'
    And param endDate = '2019-02-02'
    When method GET
    Then status 400
    And match $.error.errors contains {field:"startDate", reason:"#notnull", message:"#notnull", location:"#null", locationType:"#null"}

  Scenario: Verify issued badges report - missing end date
    Given path 'reports/issued-badges'
    And param startDate = '2019-02-01'
    When method GET
    Then status 400
    And match $.error.errors contains {field:"endDate", reason:"#notnull", message:"#notnull", location:"#null", locationType:"#null"}

  Scenario: Verify issued badges report - end date before start date
    Given path 'reports/issued-badges'
    And param startDate = '2019-02-03'
    And param endDate = '2019-02-02'
    When method GET
    Then status 400
    And match $.error.errors contains {field:"startDateAfterEndDate", reason:"#notnull", message:"#notnull", location:"#null", locationType:"#null"}




