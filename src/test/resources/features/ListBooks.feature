Feature: List books
  As a system user
  I want to see a list of books
  So that I can mark those I've read

  Background: Books already included
    Given that I have books in the database
      | title                                    | author                                        | subtitle           | publishingCompany  |
      | Design Patterns                          | Eduardo Guerra                                |                    | Caso do Codigo     |
      | Reflexão e Anotações                     | Eduardo Guerra                                |                    | Caso do Codigo     |
      | Introduction to Neural Networks for Java | Jeff Heaton                                   |                    | Heaton Research    |
      | Java 8 in Action                         | Raoul Gabriel Urma, Mario Fusco, Alan Mycroft |                    | Heaton Research    |
      | JSF e JPA                                | Gilliard Cordeiro                             |                    | Caso do  Codigo    |
      | Thinking in C++ Volume 1                 | Bruce Eckel                                   |                    | MindView Inc       |
      | abcds java sfasd                         | asfsefa                                       |                    | bbbb               |
      | abcds sfasd                              | asfs JavA                                     |                    | bbbb               |
      | abcds sfasd                              | asfs                                          | JAVA jsjsjs        | bbbb               |
      | abcds sfasd                              | asfs                                          | jsjsjs             | bbbb jAVaccc       |

  Scenario: User asks for the book list with no filter
    When I asks for the book list with filter ""
    Then I receive a list of books of size 10
    When I asks for the book list with filter "null"
    Then I receive a list of books of size 10

  Scenario: User asks for the book list with a filter
    When I asks for the book list with filter "java"
    Then I receive a list of books of size 6
