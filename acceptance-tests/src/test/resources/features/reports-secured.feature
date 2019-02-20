@badge-print-batch-secured
Feature: Verify print a batch end point is secured

  Background:
    * url baseUrl
    # Get user token, rather than inter service token
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify print a badge - valid batch type (FASTTRACK)
    Given path 'reports/issued-badges'
    And request {batchType: "FASTTRACK"}
    When method POST
    Then status 403
