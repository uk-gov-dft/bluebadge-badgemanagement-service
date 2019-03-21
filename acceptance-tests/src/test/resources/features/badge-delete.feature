@badge-delete
Feature: Verify delete a badge

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-3rd-party-scotland.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * callonce read('./badge-create-person.feature')

* def lowerCase =
  """
  function(src) {
    return src.trim().toLowerCase();
  } 
  """

  Scenario: Verify 404 response for delete of unknown badge
    Given path 'badges/AAAAAA'
    When method DELETE
    Then status 404

  Scenario: Verify delete a badge success
    Given path 'badges/'+ badgeNo
    When method DELETE
    Then status 200

  Scenario: Verify delete a badge success with badge number in lower case and badge cannot be read after
    Given path 'badges/' + lowerCase(badgeNo2)
    When method DELETE
    Then status 200
    Given path 'badges/' + lowerCase(badgeNo2)
    * header Authorization = 'Bearer ' + result.accessToken
    When method GET
    Then status 404

  Scenario: Verify delete a badge image success
    * call read('./badge-create-person.feature')
    Given path 'badges/'+ badgeNo
    When method GET
    Then status 200

    * def imageLink = $.data.imageLink
    * url imageLink
    Given path null
    When method GET
    Then status 200

    * url baseUrl
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'badges/'+ badgeNo
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
