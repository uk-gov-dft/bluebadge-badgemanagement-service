@badge-replace
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

  Scenario: Verify 404 response for replace of unknown badge
    Given path 'badges/AAAAAA/replacements'
    And request {"badgeNumber": "AAAAAA","replaceReasonCode": "STOLEN","deliverToCode": "HOME","deliveryOptionCode": "STANDARD"}
    When method POST
    Then status 404

  Scenario: Verify replace already cancelled badge
    Given path 'badges/CCCCCC/replacements'
    And request {"badgeNumber": "CCCCCC","replaceReasonCode": "DAMAGED","deliverToCode": "HOME","deliveryOptionCode": "FAST"}
    When method POST
    Then status 400
    And match $.error.errors[0].message == 'Invalid.badge.replace.badgeStatus'

  Scenario: Verify replace expired badge
    Given path 'badges/DDDDDD/replacements'
    And request {"badgeNumber": "DDDDDD","replaceReasonCode": "DAMAGED","deliverToCode": "HOME","deliveryOptionCode": "FAST"}
    When method POST
    Then status 400
    And match $.error.errors[0].message == 'Invalid.badge.replace.expiryDate'

  Scenario: Verify cancel a badge success
    Given path 'badges/'+ createdBadgeNo + '/replacements'
    And request {badgeNumber: "#(createdBadgeNo)","replaceReasonCode": "STOLEN","deliverToCode": "HOME","deliveryOptionCode": "STANDARD"}
    When method POST
    Then status 200

  Scenario: Verify replace a badge with different path and body badge numbers
    Given path 'badges/'+ createdBadgeNo + '/replacements'
    And request {"badgeNumber": "DDDDDD","replaceReasonCode": "DAMAGED","deliverToCode": "HOME","deliveryOptionCode": "FAST"}
    When method POST
    Then status 400
    And match $.error.errors[0].message == 'Invalid.badgeNumber'

  Scenario: Verify replace a badge in a different local authority
    Given path 'badges/BBBBBB/replacements'
    And request {"badgeNumber": "BBBBBB","replaceReasonCode": "STOLEN","deliverToCode": "HOME","deliveryOptionCode": "STANDARD"}
    When method POST
    Then status 403
