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
    * callonce read('./badge-create-person.feature')
    * def replaceRequest =
      """
      {
        badgeNumber: '123456',
        replaceReasonCode: 'STOLE',
        deliverToCode: 'HOME',
        deliveryOptionCode: 'FAST'
      }
      """

  Scenario: Verify 404 response for replace of unknown badge
    * set replaceRequest $.badgeNumber = 'AAAAAA'
    Given path 'badges/AAAAAA/replacements'
    And request replaceRequest
    When method POST
    Then status 404

  Scenario: Verify replace already replaced badge
    * set replaceRequest $.badgeNumber = 'CCCCCC'
    Given path 'badges/CCCCCC/replacements'
    And request replaceRequest
    When method POST
    Then status 400
    And match $.error.message == 'Invalid.badge.replace.badgeStatus'

  Scenario: Verify replace expired badge
    * set replaceRequest $.badgeNumber = 'DDDDDD'
    Given path 'badges/DDDDDD/replacements'
    And request replaceRequest
    When method POST
    Then status 400
    And match $.error.message == 'Invalid.badge.replace.expiryDate'

  Scenario: Verify replace a badge success
    * set replaceRequest $.badgeNumber = 'EEEEEF'
    * set replaceRequest $.deliveryOptionCode = 'STAND'
    Given path 'badges/EEEEEF/replacements'
    And request replaceRequest
    When method POST
    Then status 200
    * def newBadgeNo = $.data

    * def replacedBadge = db.readRow("select * from badgemanagement.badge b where b.badge_no = 'EEEEEF'")
    * match replacedBadge.badge_status == 'REPLACED'
    * match replacedBadge.replace_reason_code == 'STOLE'

    * def newBadge = db.readRow("select * from badgemanagement.badge b where b.badge_no = '" + newBadgeNo + "'")
    * match newBadge.badge_status == 'ORDERED'
    * match replacedBadge.party_code == newBadge.party_code
    * match replacedBadge.local_authority_ref == newBadge.local_authority_ref
    * match replacedBadge.app_channel_code == newBadge.app_channel_code
    * match replacedBadge.eligibility_code == newBadge.eligibility_code
    * match replacedBadge.image_link == newBadge.image_link
    * match newBadge.deliver_option_code == 'STAND'
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
    * match replacedBadge.holder_name_upper == 'REGINALD'
    * match replacedBadge.cancel_reason_code == newBadge.cancel_reason_code
    * match replacedBadge.local_authority_short_code == newBadge.local_authority_short_code
    * match replacedBadge.image_link_original == newBadge.image_link_original
    * match newBadge.replace_reason_code == null

  Scenario: Verify replace a badge success with lower case badge number
    * set replaceRequest $.badgeNumber = 'eeeeea'
    * set replaceRequest $.deliverToCode = 'COUNCIL'
    Given path 'badges/eeeeea/replacements'
    And request replaceRequest
    When method POST
    Then status 200
    * def newBadgeNo = $.data

    * def replacedBadge = db.readRow("select * from badgemanagement.badge b where b.badge_no = 'EEEEEA'")
    * match replacedBadge.badge_status == 'REPLACED'
    * match replacedBadge.replace_reason_code == 'STOLE'

    * def newBadge = db.readRow("select * from badgemanagement.badge b where b.badge_no = '" + newBadgeNo + "'")
    * match newBadge.badge_status == 'ORDERED'
    * match replacedBadge.party_code == newBadge.party_code
    * match replacedBadge.local_authority_ref == newBadge.local_authority_ref
    * match replacedBadge.app_channel_code == newBadge.app_channel_code
    * match replacedBadge.eligibility_code == newBadge.eligibility_code
    * match replacedBadge.image_link == newBadge.image_link
    * match newBadge.deliver_option_code == 'FAST'
    * match newBadge.deliver_to_code == 'COUNCIL'
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
    * match replacedBadge.holder_name_upper == 'REGINALD'
    * match replacedBadge.cancel_reason_code == newBadge.cancel_reason_code
    * match replacedBadge.local_authority_short_code == newBadge.local_authority_short_code
    * match replacedBadge.image_link_original == newBadge.image_link_original
    * match newBadge.replace_reason_code == null

  Scenario: Verify replace a badge with different path and body badge numbers
    * set replaceRequest $.badgeNumber = 'DDDDDD'
    Given path 'badges/'+ badgeNo + '/replacements'
    And request replaceRequest
    When method POST
    Then status 400
    And match $.error.message == 'Invalid.badgeNumber'

  Scenario: Verify replace a badge in a different local authority
    * set replaceRequest $.badgeNumber = 'BBBBBB'
    Given path 'badges/BBBBBB/replacements'
    And request replaceRequest
    When method POST
    Then status 403

  Scenario: Verify replace a badge with invalid deliver to enum value
    * set replaceRequest $.deliverToCode = 'INVALID'
    Given path 'badges/123456/replacements'
    And request replaceRequest
    When method POST
    Then status 400
    And match $.error.errors contains {field:'deliverToCode',reason:'#notnull',message:'InvalidFormat.deliverToCode',location:null,locationType:null}

  Scenario: Verify replace a badge with invalid deliver option enum value
    * set replaceRequest $.deliveryOptionCode = 'ARIBA'
    Given path 'badges/123456/replacements'
    And request replaceRequest
    When method POST
    Then status 400
    And match $.error.errors contains {field:'deliveryOptionCode',reason:'#notnull',message:'InvalidFormat.deliveryOptionCode',location:null,locationType:null}

  Scenario: Verify replace a badge with invalid replace reason enum value
    * set replaceRequest $.replaceReasonCode = 'ARIBA'
    Given path 'badges/123456/replacements'
    And request replaceRequest
    When method POST
    Then status 400
    And match $.error.errors contains {field:'replaceReasonCode',reason:'#notnull',message:'InvalidFormat.replaceReasonCode',location:null,locationType:null}

  Scenario: Verify replace a badge with null params
    * set replaceRequest $.replaceReasonCode = null
    * set replaceRequest $.deliveryOptionCode = null
    * set replaceRequest $.deliverToCode = null
    * set replaceRequest $.badgeNumber = null
    Given path 'badges/123456/replacements'
    And request replaceRequest
    When method POST
    Then status 400
    And match $.error.errors contains {field:'replaceReasonCode',reason:'#notnull',message:'NotNull.badgeReplaceRequest.replaceReasonCode',location:null,locationType:null}
    And match $.error.errors contains {field:'deliverToCode',reason:'#notnull',message:'NotNull.badgeReplaceRequest.deliverToCode',location:null,locationType:null}
    And match $.error.errors contains {field:'badgeNumber',reason:'#notnull',message:'NotNull.badgeReplaceRequest.badgeNumber',location:null,locationType:null}
    And match $.error.errors contains {field:'deliveryOptionCode',reason:'#notnull',message:'NotNull.badgeReplaceRequest.deliveryOptionCode',location:null,locationType:null}