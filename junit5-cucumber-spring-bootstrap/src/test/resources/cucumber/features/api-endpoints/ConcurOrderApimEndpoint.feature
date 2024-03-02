@api-endpoint @ConcurOrderApimEndpoint
Feature: As a business analyst, I want to test Concur order flow processing from API-M to SAP AIF through SAP CI
    Scenario: API-M should not allow to post order xml with empty credentials
        Given I am an unauthorized sender
        When I hit apim endpoint with empty authentication
        Then I can see status code "401"
        And I can see error message "Username Password required"

    Scenario: API-M should not allow to post order xml with incorrect credentials
        Given I am an unauthorized sender
        When I hit apim endpoint with incorrect credentials
        Then I can see status code "401"
        And I can see error message "Username or Password incorrect"

    Scenario: API-M should be reachable with valid credentials
        Given I am an authorized sender
        When I hit apim endpoint with valid credentials
        Then I can see status code "200"

    Scenario: API-M should validate invalid order xml
        Given I am an authenticated sender
        When I hit apim endpoint with invalid order xml
        Then I can see status code "200"

    Scenario: API-M should processs valid order xml
        Given I am an authenticated sender
        When I hit apim endpoint with valid order xml
        Then I can see status code "200"