@badge-reprint-secured
Feature: Verify badge re-print end points are secured

  Background:
    * url baseUrl
    * header Accept = jsonVersionHeader

  Scenario: Verify retrieve a badge without oauth header is denied
    Given path 'badges/print-batch/1'
    Given request {}
    When method POST
    Then status 401
