@users-get
Feature: Verify users retrieval

  Background:
  * url baseUrl

#  Scenario: Verify retrieve all users for an authority
#    Given path 'authorities/2/users'
#    When method GET
#    Then status 200
#    And match each $.data.users contains {id:"#notnull", name:"#notnull", emailAddress:"#notnull"}
