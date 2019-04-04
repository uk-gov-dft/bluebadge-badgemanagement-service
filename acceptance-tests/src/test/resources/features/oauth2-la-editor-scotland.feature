@authentication
Feature: Authenticate with the authorisation service

  Background:
    * url authServerUrl

  Scenario: Obtain access token with client credentials

    * path 'oauth/token'
    * header Authorization = call read('classpath:basic-auth.js') { username: 'bb_la_web_app_id',  ***REMOVED*** }
    * form field grant_type = 'password'
    * form field clientId = 'bb_la_web_app_id'
    * form field username = 'editor@dft.gov.uk'
    * form field  ***REMOVED***
    * method post
    * status 200
    * match $.access_token == '#notnull'
    * match $.token_type == 'bearer'
    * def accessToken = response.access_token