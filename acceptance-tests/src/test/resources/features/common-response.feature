@common-response
Feature: Verify Responses wrap a CommonResponse

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-3rd-party-scotland.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader
    * def testData = read('classpath:test-data.json')

  Scenario: Verify create 400 with common response for bean validation
    * set testData.personBadge.party.contact.postCode = 'S'
    * set testData.personBadge.party.person.genderCode = null
    Given path 'badges'
    And request testData.personBadge
    When method POST
    Then status 400
    And match $.error.errors contains {field:"party.contact.postCode", reason:"#notnull", message:"#notnull", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"party.person.genderCode", reason:"#notnull", message:"NotNull.badgeOrderRequest.party.person.genderCode", location:"#null", locationType:"#null"}

  Scenario: Jackson deserialize exception wrapped in common response
    * set testData.personBadge.numberOfBadges = 'not a number'
    Given path 'badges'
    And request testData.personBadge
    When method POST
    Then status 400
    And match $.error.errors contains {field:"numberOfBadges", reason:"`not a number` is not a valid Integer.", message:"#notnull", location:"#null", locationType:"#null"}
