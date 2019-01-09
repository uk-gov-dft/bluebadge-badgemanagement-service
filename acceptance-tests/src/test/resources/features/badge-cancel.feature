@badge-cancel
Feature: Verify cancel a badge

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * def createResult = callonce read('./badge-create-person.feature')
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

  Scenario: Verify cancel a badge success when newly created
    Given path 'badges/'+ createdBadgeNo + '/cancellations'
    And request {badgeNumber: "#(createdBadgeNo)", cancelReasonCode: "NOLONG"}
    When method POST
    Then status 200

  Scenario: Verify cancel fails for invalid status
    Given path 'badges/BBBBBD/cancellations'
    And request {badgeNumber: "BBBBBD", cancelReasonCode: "NOLONG"}
    When method POST
    Then status 400
    And match $.error.message == 'Invalid.badge.cancel.status'

  Scenario: Verify cancel a badge success when ISSUED
    Given path 'badges/BBBBBC/cancellations'
    And request {badgeNumber: "BBBBBC", cancelReasonCode: "NOLONG"}
    When method POST
    Then status 200

  Scenario: Verify cancel a badge success when ISSUED
    Given path 'badges/bbbbbe/cancellations'
    And request {badgeNumber: "bbbbbe", cancelReasonCode: "NOLONG"}
    When method POST
    Then status 200

  Scenario: Verify cancel a badge with different path and body badge numbers
    Given path 'badges/'+ createdBadgeNo + '/cancellations'
    And request {badgeNumber: "BBBBBB", cancelReasonCode: "NOLONG"}
    When method POST
    Then status 400
    And match $.error.errors[0].message == 'Invalid.badgeNumber'

  Scenario: Verify cancel a badge in a different local authority
    Given path 'badges/BBBBBB/cancellations'
    And request {badgeNumber: "BBBBBB", cancelReasonCode: "NOLONG"}
    When method POST
    Then status 403
