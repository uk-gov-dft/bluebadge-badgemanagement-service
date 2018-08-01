@badge-get
Feature: Verify badge details

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * def createResult = callonce read('./create.org.badge.feature')
    * def createdBadgeNo = createResult.badgeNo

  Scenario: Verify retrieve a badge
    Given path 'badges/' + createdBadgeNo
    When method GET
    Then status 200
    And match $.data.party.contact.fullName contains 'June Whitfield'
    And def apptime = $.data

    * print '=============================' + apptime

  Scenario: Verify retrieve responds with 404 for unknown badge number
    Given  path 'badges/ZZZZZZ'
    When method GET
    Then status 404
    And match $.error.message contains 'NotFound.badge'