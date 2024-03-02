@GooglePage @ui
Feature: As a customer when I enter google URL, it should take me to google homepage

    Scenario: I hit google URL, it should take me to me to google homepage
        Given a web browser
        When I hit google url
        Then validate google homepage title "Google"
        And page contains a search bar with action "https://www.google.com/search"
        And page contains google brand image with alt "Google"