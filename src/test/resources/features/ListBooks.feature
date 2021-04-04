Feature: List books
  As a system user
  I want to see a list of books
  So that I can mark those I've read

  Background: Books already included
    Given that I have books in the database

  Scenario: User asks for the book list
    When I ask for the book list
    Then I receive a list of books of size 8
