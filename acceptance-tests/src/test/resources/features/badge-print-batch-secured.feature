@badge-print-batch-secured
Feature: Verify print a batch end point is secured

  Background:
    * url baseUrl
    # Get user token, rather than inter service token
    * def result = callonce read('./oauth2-3rd-party-scotland.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader

  Scenario: Verify print a badge - valid batch type (FASTTRACK)
    Given path 'badges/print-batch'
    And request {batchType: "FASTTRACK"}
    When method POST
    Then status 403
