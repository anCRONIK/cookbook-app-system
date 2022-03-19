Feature: Measurement units controller testing

  Scenario: Fetching all measurement units
    When the client calls /api/v1/measurement-units with http method GET
    Then the client receives status code of 200
    And the client receives valid measurement units