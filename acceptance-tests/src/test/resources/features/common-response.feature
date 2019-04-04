@common-response
Feature: Verify Responses wrap a CommonResponse

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-3rd-party-scotland.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify create 400 with common response for bean validation
    * def badge =
    """
      {
      party: {
        typeCode: 'PERSON',
        contact: {
          fullName: 'June Whitfield',
          buildingStreet: '65 Basil Chambers',
          line2: 'Northern Quarter',
          townCity: 'Manchester',
          postCode: 'S',
          primaryPhoneNumber: '01616548765',
          secondaryPhoneNumber: '01616548765',
          emailAddress: 'june@bigbrainknitting.com'
        },
        person: {
          badgeHolderName: 'TestData Bloggs',
          nino: 'NY188796B',
          dob: '1972-09-12',
          genderCode: null
        },
        organisation: {
          badgeHolderName: 'The Monroe Institute'
        }
      },
      localAuthorityShortCode: 'GLAM',
      localAuthorityRef: 'YOURCODE',
      applicationDate: '2018-04-23',
      applicationChannelCode: 'ONLINE',
      startDate: '#(futureDate)',
      expiryDate: '#(futureDatePlusYear)',
      eligibilityCode: 'CHILDBULK',
      imageFile: 'YWZpbGU=',
      deliverToCode: 'HOME',
      deliveryOptionCode: 'STAND',
      numberOfBadges: 1
    }
    """

    Given path 'badges'
    And request badge
    When method POST
    Then status 400
    And match $.error.errors contains {field:"party.contact.postCode", reason:"#notnull", message:"#notnull", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"party.person.genderCode", reason:"#notnull", message:"NotNull.badgeOrderRequest.party.person.genderCode", location:"#null", locationType:"#null"}

    Scenario: Jackson deserialize exception wrapped in common response
      * def badge =
    """
      {
      party: {
        typeCode: 'PERSON',
        contact: {
          fullName: 'June Whitfield',
          buildingStreet: '65 Basil Chambers',
          line2: 'Northern Quarter',
          townCity: 'Manchester',
          postCode: 'WV164AW',
          primaryPhoneNumber: '01616548765',
          secondaryPhoneNumber: '01616548765',
          emailAddress: 'june@bigbrainknitting.com'
        },
        person: {
          badgeHolderName: 'TestData Bloggs',
          nino: 'NY188796B',
          dob: '1972-09-12',
          genderCode: 'MALE'
        },
        organisation: {
          badgeHolderName: 'The Monroe Institute'
        }
      },
      localAuthorityShortCode: 'GLAM',
      localAuthorityRef: 'YOURCODE',
      applicationDate: '2018-04-23',
      applicationChannelCode: 'ONLINE',
      startDate: '#(futureDate)',
      expiryDate: '#(futureDatePlusYear)',
      eligibilityCode: 'CHILDBULK',
      imageFile: 'YWZpbGU=',
      deliverToCode: 'HOME',
      deliveryOptionCode: 'STAND',
      numberOfBadges: "not a number"
    }
    """

      Given path 'badges'
      And request badge
      When method POST
      Then status 400
      And match $.error.errors contains {field:"numberOfBadges", reason:"`not a number` is not a valid Integer.", message:"#notnull", location:"#null", locationType:"#null"}
