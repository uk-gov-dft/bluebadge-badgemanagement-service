@create-org-badge
Feature: Verify Create badge of type org

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify valid create
    * def badge =
    """
    {
    party: {
    typeCode: 'ORG',
    contact: {
    fullName: 'June Whitfield',
    buildingStreet: '65 Basil Chambers',
    line2: 'Northern Quarter',
    townCity: 'Manchester',
    postCode: 'OR6 8GG',
    primaryPhoneNumber: '01616548765',
    secondaryPhoneNumber: '01616548765',
    emailAddress: 'june@bigbrainknitting.com'
    },
    organisation: {
    badgeHolderName: 'ORGTEST1234'
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
    Then status 200
    And match $.data[*] contains "#notnull"
    * def badgeNo = $.data[0]