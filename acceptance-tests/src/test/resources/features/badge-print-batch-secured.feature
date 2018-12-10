@badge-print-batch-secured
Feature: Verify print a batch end point is secured

  Background:
    * url baseUrl

  Scenario: Verify print a badge - valid batch type (FASTTRACK)
    Given path 'badges/print-batch'
    And request {badgeType: "FASTTRACK"}
    When method POST
    Then status 401
