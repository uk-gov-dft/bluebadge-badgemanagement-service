@badge-create-business-rule-bad
Feature: Verify Create badge with 400

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify create 400 with business validation start in past
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
        startDate: '2016-06-30',
        expiryDate: '2017-07-01',
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
    And match $.error.errors contains {field:"startDate", reason:"#notnull", message:"#notnull", location:"#null", locationType:"#null"}