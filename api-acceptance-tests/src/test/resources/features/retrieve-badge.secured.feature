@users-put
Feature: Verify badge end points are secured

  Background:
    * url baseUrl
    * def createResult = callonce read('./create.org.badge.feature')
    * def createdBadgeNo = createResult.badgeNo

  Scenario: Verify retrieve a badge without oauth header is denied
    Given path 'badges/' + createdBadgeNo
    When method GET
    Then status 401
