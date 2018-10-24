@badge-delete
Feature: Verify delete a badge

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify 404 response for delete of unknown badge
    Given path 'badges/AAAAAA'
    When method DELETE
    Then status 404

  Scenario: Verify delete a badge success
    * def createResult = call read('./badge-create-person.feature')
    * def createdBadgeNo = createResult.badgeNo
    Given path 'badges/'+ createdBadgeNo
    When method DELETE
    Then status 200

  Scenario: Verify retrieve is not found after delete
    * def createResult = call read('./badge-create-person.feature')
    * def createdBadgeNo = createResult.badgeNo
    Given path 'badges/'+ createdBadgeNo
    When method DELETE
    Then status 200
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'badges/'+ createdBadgeNo
    When method GET
    Then status 404

  Scenario: Verify delete a badge image success
    * def createResult = call read('./badge-create-person.feature')
    * def createdBadgeNo = createResult.badgeNo
    Given path 'badges/'+ createdBadgeNo
    When method GET
    Then status 200

    * def imageLink = $.data.imageLink
    * url imageLink
    Given path null
    When method GET
    Then status 200

    * url baseUrl
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'badges/'+ createdBadgeNo
    When method DELETE
    Then status 200

    * url imageLink
    Given path null
    When method GET
    Then status 404

  Scenario: Verify delete a badge in a different local authority
    Given path 'badges/BBBBBB'
    When method DELETE
    Then status 403
