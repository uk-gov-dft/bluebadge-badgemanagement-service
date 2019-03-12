@badge-retrieve-in-batches
Feature: Verify retrieve badge details

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data-retrieve-with-badges.sql')
    * def result = callonce read('./oauth2-3rd-party-scotland.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify retrieve a badge - sent to printer
    Given path 'badges/NNNJMJ'
    When method GET
    Then status 200
    And match $.data.badgeNumber contains 'NNNJMJ'
    And match $.data.printRequestDateTime contains '2019-03-07T01:01:00'
    And match $.data.issuedDate == null
    And match $.data.rejectedReason == null

  Scenario: Verify retrieve a badge - issued
    Given path 'badges/NNNJMH'
    When method GET
    Then status 200
    And match $.data.badgeNumber contains 'NNNJMH'
    And match $.data.printRequestDateTime contains '2019-03-07T01:01:00'
    And match $.data.issuedDate contains '2019-03-07T01:02:00'
    And match $.data.rejectedReason contains null

  Scenario: Verify retrieve a badge - issued
    Given path 'badges/NNNJMF'
    When method GET
    Then status 200
    And match $.data.badgeNumber contains 'NNNJMF'
    And match $.data.printRequestDateTime contains '2019-03-07T01:03:00'
    And match $.data.issuedDate contains null
    And match $.data.rejectedReason contains 'my rejected reason'
