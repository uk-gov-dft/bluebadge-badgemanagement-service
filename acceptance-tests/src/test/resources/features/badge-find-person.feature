@badge-find-person
Feature: Verify find badge for newly created badge

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-3rd-party-scotland.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * callonce read('./badge-create-person.feature')

  Scenario: Verify findBadges finds the created badge
    Given path 'badges'
    And param name = 'red bloggs'
    When method GET
    Then status 200
    And match $.data[*] contains "#notnull"
    And match $.data[*].badgeNumber contains badgeNo

  Scenario: Verify findBadges finds nothing for unknown name
    Given path 'badges'
    And param name = 'IDONOTEXISTIWOULDHOPE'
    When method GET
    Then status 200
    And def stuff = $.data
    And assert stuff.length == 0

  Scenario: Verify findBadges bad request when missing name and postcode
    Given path 'badges'
    And param name = ''
    And param postCode = ''
    When method GET
    Then status 400
    And match $.error.message == 'NotNull.params.badge.find'

  Scenario: Verify findBadges bad request when name and postcode both provided
    Given path 'badges'
    And param name = 'CANTHAVENAMEANDPOSTCODE'
    And param postCode = 'WV164AW'
    When method GET
    Then status 400
    And match $.error.message == 'TooMany.params.badge.find'
