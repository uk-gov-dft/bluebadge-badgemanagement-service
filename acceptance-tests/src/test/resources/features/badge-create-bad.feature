@badge-create-bad
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
      numberOfBadges: 1
    }
    """

    Given path 'badges'
    And request badge
    When method POST
    Then status 400
    And match $.error.errors contains {field:"party.contact.postCode", reason:"#notnull", message:"#notnull", location:"#null", locationType:"#null"}

   Scenario: Verify create more than 1 badge for person 400
    * def badgeNotValidNumberPerson =
    """
      {
      party: {
        typeCode: 'PERSON',
        contact: {
          fullName: 'June Whitfield',
          buildingStreet: '65 Basil Chambers',
          line2: 'Northern Quarter',
          townCity: 'Manchester',
          postCode: 'WK6 8GH',
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
      localAuthorityShortCode: 'ABERD',
      localAuthorityRef: 'YOURCODE',
      applicationDate: '2018-04-23',
      applicationChannelCode: 'ONLINE',
      startDate: '#(futureDate)',
      expiryDate: '#(futureDatePlusYear)',
      eligibilityCode: 'CHILDBULK',
      imageFile: 'YWZpbGU=',
      deliverToCode: 'HOME',
      deliveryOptionCode: 'STAND',
      numberOfBadges: 5
    }
    """

    Given path 'badges'
    And request badgeNotValidNumberPerson
    When method POST
    Then status 400
    And match $.error.errors contains {field:"numberOfBadges", reason:"#notnull", message:"#notnull", location:"#null", locationType:"#null"}
    
   Scenario: Verify create more than 50 badge for organisation 400
    * def badgeNotValidNumberOrg =
    """
      {
      party: {
        typeCode: 'ORG',
        contact: {
          fullName: 'June Whitfield',
          buildingStreet: '65 Basil Chambers',
          line2: 'Northern Quarter',
          townCity: 'Manchester',
          postCode: 'WK6 8GH',
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
      localAuthorityShortCode: 'ABERD',
      localAuthorityRef: 'YOURCODE',
      applicationDate: '2018-04-23',
      applicationChannelCode: 'ONLINE',
      startDate: '#(futureDate)',
      expiryDate: '#(futureDatePlusYear)',
      eligibilityCode: 'CHILDBULK',
      imageFile: 'YWZpbGU=',
      deliverToCode: 'HOME',
      deliveryOptionCode: 'STAND',
      numberOfBadges: 105
    }
    """

    Given path 'badges'
    And request badgeNotValidNumberOrg
    When method POST
    Then status 400
    And match $.error.errors contains {field:"numberOfBadges", reason:"#notnull", message:"#notnull", location:"#null", locationType:"#null"}
    