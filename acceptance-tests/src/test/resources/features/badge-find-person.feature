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
    And match $.pagingInfo.count == 1
    And match $.pagingInfo.total == 1
    And match $.pagingInfo.pageNum == 1
    And match $.pagingInfo.pageSize == 50
    And match $.pagingInfo.pages == 1

  Scenario: Verify findBadges finds the created badge second page empty
    Given path 'badges'
    And param name = 'red bloggs'
    When method GET
    Then status 200
    And match $.pagingInfo.count == 1
    And match $.pagingInfo.total == 1
    And match $.pagingInfo.pageNum == 1
    And match $.pagingInfo.pageSize == 50
    And match $.pagingInfo.pages == 1

  Scenario: Verify findBadges finds the created badge second page not empty (pageNum=2 and pageSize=1)
    Given path 'badges'
    And param name = 'TestData'
    And param pageNum = 2
    And param pageSize = 1
    When method GET
    Then status 200
    And match $.data[*] contains "#notnull"
    And match $.data[*].badgeNumber contains badgeNo2
    And match $.pagingInfo.count == 1
    And match $.pagingInfo.total == 2
    And match $.pagingInfo.pageNum == 2
    And match $.pagingInfo.pageSize == 1
    And match $.pagingInfo.pages == 2

  Scenario: Verify findBadges finds the created badge second page not empty (pageNum=1 and pageSize=2)
    Given path 'badges'
    And param name = 'TestData'
    And param pageNum = 1
    And param pageSize = 2
    When method GET
    Then status 200
    And match $.data[*] contains "#notnull"
    And match $.data[*].badgeNumber contains badgeNo
    And match $.data[*].badgeNumber contains badgeNo2
    And match $.pagingInfo.count == 2
    And match $.pagingInfo.total == 2
    And match $.pagingInfo.pageNum == 1
    And match $.pagingInfo.pageSize == 2
    And match $.pagingInfo.pages == 1

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
