Feature: Edit A Rectangle
  Scenario: User resize the rectangle
    Given The user has an existing rectangle
    When When the user selects one of the rectangle's corners
    When The mouse is moved on x or y positions
    Then The rectangle should be resized


