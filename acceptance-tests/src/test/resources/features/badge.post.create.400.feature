@badge-post-400
Feature: Verify Create badge with 400

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify create 400
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
          badgeHolderName: 'Fred Bloggs',
          nino: 'NY188796B',
          dob: '1972-09-12',
          genderCode: 'MALE'
        },
        organisation: {
          badgeHolderName: 'The Monroe Institute'
        }
      },
      localAuthorityId: 187,
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
