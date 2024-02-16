@ConcurOrderApimEndpoint
Feature: As a business analyst, I want to test Concur order flow processing from API-M to SAP AIF through SAP CI
    Scenario: API-M should not allow to post order xml with incorrect credentials
        Given I am an unauthorized sender
        When I hit apim endpoint with empty authentication
        Then I can see error code
            |errorCode|
            |401|
        And I can see error message
            |errorMsg|
            |Username Password required|