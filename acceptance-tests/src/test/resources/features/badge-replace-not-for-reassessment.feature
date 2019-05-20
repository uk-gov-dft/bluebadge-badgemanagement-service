@badge-replace
Feature: Verify replace a badge

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-3rd-party-scotland.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader

  Scenario: Verify replace a badge will not alter not-for-reassessment flag being NULL
    Given path 'badges/EEEEEF/replacements'
    And request {badgeNumber: "EEEEEF","replaceReasonCode": "STOLE","deliverToCode": "HOME","deliveryOptionCode": "STAND"}
    When method POST
    Then status 200
    * def newBadgeNo = $.data

    * def replacedBadge = db.readRow("select * from badgemanagement.badge b where b.badge_no = 'EEEEEF'")
    * match replacedBadge.badge_status == 'REPLACED'
    * match replacedBadge.replace_reason_code == 'STOLE'

    * def newBadge = db.readRow("select * from badgemanagement.badge b where b.badge_no = '" + newBadgeNo + "'")
    * match newBadge.badge_status == 'ORDERED'
    * match replacedBadge.not_for_reassessment == newBadge.not_for_reassessment

  Scenario: Verify replace a badge success not alter not-for-reassessment flag being TRUE
    Given path 'badges/eeeeea/replacements'
    And request {badgeNumber: "EEEEEA","replaceReasonCode": "STOLE","deliverToCode": "HOME","deliveryOptionCode": "STAND"}
    When method POST
    Then status 200
    * def newBadgeNo = $.data

    * def replacedBadge = db.readRow("select * from badgemanagement.badge b where b.badge_no = 'EEEEEA'")
    * match replacedBadge.badge_status == 'REPLACED'
    * match replacedBadge.replace_reason_code == 'STOLE'

    * def newBadge = db.readRow("select * from badgemanagement.badge b where b.badge_no = '" + newBadgeNo + "'")
    * match newBadge.badge_status == 'ORDERED'
    * match replacedBadge.not_for_reassessment == newBadge.not_for_reassessment


