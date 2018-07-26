@badge-find-org
Feature: Verify retrieve newly created org badge

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * def createResult = callonce read('./create.org.badge.feature')
    * def createdBadgeNo = createResult.badgeNo

  Scenario: Verify findBadges by name
    Given path 'badges'
    And param name = 'orgtest'
    When method GET
    Then status 200
    And match $.data[*].badgeNumber contains createdBadgeNo

  Scenario: Verify findBadges by postcode
    Given path 'badges'
    And param postCode = 'OR68GG'
    When method GET
    Then status 200
    And match $.data[*].badgeNumber contains createdBadgeNo