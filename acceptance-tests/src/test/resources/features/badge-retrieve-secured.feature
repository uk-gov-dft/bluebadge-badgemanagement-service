@badge-retrieve-secured
Feature: Verify badge end points are secured

  Background:
    * url baseUrl

  Scenario: Verify retrieve a badge without oauth header is denied
    Given path 'badges/AAAAAA'
    When method GET
    Then status 401
