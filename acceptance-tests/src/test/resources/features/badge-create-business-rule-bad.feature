@badge-create-business-rule-bad
Feature: Verify Create badge with 400

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-3rd-party-scotland.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    * def testData = read('classpath:test-data.json')

  Scenario: Verify create 400 with business validation start in past
    * set testData.personBadge.startDate = '2016-06-30'
    Given path 'badges'
    And request testData.personBadge
    When method POST
    Then status 400
    And match $.error.errors contains {field:"startDate", reason:"#notnull", message:"#notnull", location:"#null", locationType:"#null"}

  Scenario: Verify create 400 when fast delivery option selected with council as delivery to
    * set testData.personBadge.deliverToCode = 'COUNCIL'
    * set testData.personBadge.deliveryOptionCode = 'FAST'
    Given path 'badges'
    And request testData.personBadge
    When method POST
    Then status 400
    And match $.error.errors contains {field:"deliverToCode", reason:"Only 'standard' delivery option is available when delivering to council.", message:"Invalid.badge.deliverOptionCode", location:"#null", locationType:"#null"}

  Scenario: Verify create 400 when address field character limits are exceeded
    * set testData.personBadge.party.contact.townCity = 'This string is just over forty characters'
    Given path 'badges'
    And request testData.personBadge
    When method POST
    Then status 400
    And match $.error.errors contains {"field":"party.contact.townCity","reason":"size must be between 0 and 40","message":"Size.badgeOrderRequest.party.contact.townCity","location":null,"locationType":null},{"field":"party.contact.buildingStreet","reason":"size must be between 0 and 50","message":"Size.badgeOrderRequest.party.contact.buildingStreet","location":null,"locationType":null},{"field":"party.contact.line2","reason":"size must be between 0 and 40","message":"Size.badgeOrderRequest.party.contact.line2","location":null,"locationType":null}

  Scenario: Verify create 400 when application date is set in the future
    * set testData.personBadge.applicationDate = '2027-07-01'
    Given path 'badges'
    And request testData.personBadge
    When method POST
    Then status 400
    And match $.error.errors contains {"field":"applicationDate","reason":"Application date must be in the past.","message":"DateInPast.badge.applicationDate","location":null,"locationType":null}
    