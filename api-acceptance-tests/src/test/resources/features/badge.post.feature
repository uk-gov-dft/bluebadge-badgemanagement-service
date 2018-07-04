@badge-post
Feature: Verify Create badge

  Background:
    * url baseUrl

  Scenario: Verify valid create
    * def payloadok =
    """
      {
        "party": {
          "typeCode": "PERSON",
          "contact": {
            "fullName": "June Whitfield",
            "buildingStreet": "65 Basil Chambers",
            "line2": "Northern Quarter",
            "townCity": "Manchester",
            "postCode": "SK6 8GH",
            "primaryPhoneNumber": "01616548765",
            "secondaryPhoneNumber": "01616548765",
            "emailAddress": "june@bigbrainknitting.com"
          },
          "person": {
            "badgeHolderName": "Fred Bloggs",
            "nino": "NY188796B",
            "dob": "1972-09-12",
            "genderCode": "MALE"
          },
          "organisation": {
            "badgeHolderName": "The Monroe Institute"
          }
        },
        "localAuthorityId": 187,
        "localAuthorityRef": "YOURCODE",
        "applicationDate": "2018-04-23",
        "applicationChannelCode": "ONLINE",
        "startDate": "2019-06-30",
        "expiryDate": "2019-07-01",
        "eligibilityCode": "CHILDBULK",
        "imageFile": "YWZpbGU=",
        "deliverToCode": "HOME",
        "deliveryOptionCode": "STANDARD",
        "numberOfBadges": 1
      }
    """

    * json jsonpayloadcreate = payloadok

    Given path 'badges'
    And request jsonpayloadcreate
    When method POST
    Then status 200
    And match $.data[*] contains {id:"#notnull"}
