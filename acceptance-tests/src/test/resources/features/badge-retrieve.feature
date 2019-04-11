@badge-retrieve
Feature: Verify retrieve badge details

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-3rd-party-scotland.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * callonce read('./badge-create-person.feature')

  Scenario: Verify retrieve a badge
    Given path 'badges/' + badgeNo
    When method GET
    Then status 200
    And match $.data.party.contact.fullName contains 'June Whitfield'
    And match $.data.party.contact.primaryPhoneNumber == '01616548765'
    And match $.data.party.contact.secondaryPhoneNumber == '+441616548765'
    And def apptime = $.data

  Scenario: Verify retrieve responds with 404 for unknown badge number
    Given  path 'badges/ZZZZZZ'
    When method GET
    Then status 404
    And match $.error.message contains 'NotFound.badge'

  Scenario: Verify retrieve of a replaced badge
    Given path 'badges/EEEEEE'
    When method GET
    Then status 200
    And match $.data.replaceReasonCode contains 'DAMAGED'
