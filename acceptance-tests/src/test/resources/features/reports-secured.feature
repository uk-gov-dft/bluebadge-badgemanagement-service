@reports-secured
Feature: Verify report end points are secured

  Background:
    * url baseUrl
    # Get user token, rather than inter service token
    * def result = callonce read('./oauth2-3rd-party-scotland.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader

  Scenario: Verify issued badge report is secured
    Given path 'reports/issued-badges'
    And param startDate = '2019-02-01'
    And param endDate = '2019-02-02'
    When method GET
    Then status 403
