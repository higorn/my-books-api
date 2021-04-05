package higor.mybooks.it.listbooks;

import com.github.tomakehurst.wiremock.WireMockServer;
import higor.mybooks.TestConstatns;
import higor.mybooks.api.dto.BookDto;
import higor.mybooks.application.facade.BookFacade;
import higor.mybooks.domain.page.Page;
import higor.mybooks.domain.page.PageRequest;
import higor.mybooks.it.WireMockConfig;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ContextConfiguration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = { WireMockConfig.class })
public class ListBooksSteps {

  @Autowired
  private BookFacade      bookFacade;
  @Autowired
  private WireMockServer  wireMockServer;
  @MockBean
  private JwtDecoder      jwtDecoder;

  private Page<BookDto> booksPage;

  @Given("that I have books in the database")
  public void that_I_have_books_in_the_database() {
    wireMockServer.stubFor(get(urlMatching("/v1/books/search/by-term.*")).willReturn(
        aResponse().withStatus(HttpStatus.OK.value()).withHeader("Content-Type", "application/hal+json")
            .withBody(TestConstatns.EXPECTED_BOOKS_PAGE)));
  }

  @When("I ask for the book list")
  public void i_ask_for_the_book_list() {
    booksPage = bookFacade.search("", 0, 10, "title", PageRequest.SortDirection.ASC);
  }

  @Then("I receive a list of books of size {int}")
  public void i_receive_a_list_of_books_of_size(Integer size) {
    assertEquals(size, booksPage.getContent().size());
  }
}
