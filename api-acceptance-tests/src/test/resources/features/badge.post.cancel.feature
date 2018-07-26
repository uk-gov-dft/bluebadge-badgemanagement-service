@badge-cancel-200
Feature: Verify cancel a badge

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * def createResult = callonce read('./badge.post.create.ok.feature')
    * def createdBadgeNo = createResult.badgeNo

  Scenario: Verify 404 response for cancel of unknown badge
    Given path 'badges/AAAAAA/cancellations'
    And request {badgeNumber: "AAAAAA", cancelReasonCode: "NOLONG"}
    When method POST
    Then status 404

  Scenario: Verify cancel a badge invalid code
    Given path 'badges/'+ createdBadgeNo + '/cancellations'
    And request {badgeNumber: "#(createdBadgeNo)", cancelReasonCode: "WRONGCODE"}
    When method POST
    Then status 400
    And match $.error.errors[0].message == 'Invalid.badgeCancelRequest.cancelReasonCode'

  Scenario: Verify cancel a badge success
    Given path 'badges/'+ createdBadgeNo + '/cancellations'
    And request {badgeNumber: "#(createdBadgeNo)", cancelReasonCode: "NOLONG"}
    When method POST
    Then status 200
