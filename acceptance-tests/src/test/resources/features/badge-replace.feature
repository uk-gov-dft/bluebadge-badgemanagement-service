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
    And match $.error.message == 'Invalid.badge.replace.badgeStatus'

  Scenario: Verify replace expired badge
    Given path 'badges/DDDDDD/replacements'
    And request {"badgeNumber": "DDDDDD","replaceReasonCode": "DAMAGED","deliverToCode": "HOME","deliveryOptionCode": "FAST"}
    When method POST
    Then status 400
    And match $.error.message == 'Invalid.badge.replace.expiryDate'


  Scenario: Verify replace a badge success
    Given path 'badges/'+ createdBadgeNo + '/replacements'
    And request {badgeNumber: "#(createdBadgeNo)","replaceReasonCode": "DAMAGED","deliverToCode": "HOME","deliveryOptionCode": "STANDARD"}
    When method POST
    Then status 200
    * def newBadgeNo = $.data

	* def replacedBadge = db.readRow("select * from badgemanagement.badge b where b.badge_no = '" + createdBadgeNo + "'")
	* match replacedBadge.badge_status == 'REPLACED'
	* match replacedBadge.replace_reason_code == 'DAMAGED'

	* def newBadge = db.readRow("select * from badgemanagement.badge b where b.badge_no = '" + newBadgeNo + "'")	    
	* match newBadge.badge_status == 'ISSUED'
	* match replacedBadge.party_code == newBadge.party_code
	* match replacedBadge.local_authority_ref == newBadge.local_authority_ref
	* match replacedBadge.app_channel_code == newBadge.app_channel_code
	* match replacedBadge.eligibility_code == newBadge.eligibility_code
	* match replacedBadge.image_link == newBadge.image_link
	* match newBadge.deliver_option_code == 'STANDARD'
	* match newBadge.deliver_to_code == 'HOME'
	* match replacedBadge.holder_name == newBadge.holder_name
	* match replacedBadge.nino == newBadge.nino
	* match replacedBadge.gender_code == newBadge.gender_code
	* match replacedBadge.contact_name == newBadge.contact_name
	* match replacedBadge.contact_building_street == newBadge.contact_building_street
	* match replacedBadge.contact_line2 == newBadge.contact_line2
	* match replacedBadge.contact_town_city == newBadge.contact_town_city
	* match replacedBadge.contact_postcode == newBadge.contact_postcode
	* match replacedBadge.primary_phone_no == newBadge.primary_phone_no
	* match replacedBadge.secondary_phone_no == newBadge.secondary_phone_no
	* match replacedBadge.contact_email_address == newBadge.contact_email_address
	* match replacedBadge.holder_name_upper == newBadge.holder_name_upper
	* match replacedBadge.cancel_reason_code == newBadge.cancel_reason_code
	* match replacedBadge.local_authority_short_code == newBadge.local_authority_short_code
	* match replacedBadge.image_link_original == newBadge.image_link_original
	* match newBadge.replace_reason_code == null

  Scenario: Verify replace a badge with different path and body badge numbers
    Given path 'badges/'+ createdBadgeNo + '/replacements'
    And request {"badgeNumber": "DDDDDD","replaceReasonCode": "DAMAGED","deliverToCode": "HOME","deliveryOptionCode": "FAST"}
    When method POST
    Then status 400
    And match $.error.message == 'Invalid.badgeNumber'

  Scenario: Verify replace a badge in a different local authority
    Given path 'badges/BBBBBB/replacements'
    And request {"badgeNumber": "BBBBBB","replaceReasonCode": "STOLEN","deliverToCode": "HOME","deliveryOptionCode": "STANDARD"}
    When method POST
    Then status 403
