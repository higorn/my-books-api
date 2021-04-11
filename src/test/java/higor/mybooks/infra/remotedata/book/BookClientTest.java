package higor.mybooks.infra.remotedata.book;

import com.github.tomakehurst.wiremock.WireMockServer;
import feign.RequestInterceptor;
import higor.mybooks.TestConstatns;
import higor.mybooks.domain.book.Book;
import higor.mybooks.domain.page.Page;
import higor.mybooks.domain.page.PageRequest;
import higor.mybooks.it.WireMockConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ContextConfiguration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ContextConfiguration(classes = { WireMockConfig.class })
class BookClientTest {

  @Autowired
  private BookClient         bookClient;
  @Autowired
  private WireMockServer     wireMockServer;
  @MockBean
  private RequestInterceptor requestInterceptor;
  @MockBean
  private JwtDecoder         jwtDecoder;

  @Test
  void givenATerm_thenReturnsAllBooksContainingThatTerm() {
    wireMockServer.stubFor(get(urlMatching("/v1/books/search/by-term.*"))
        .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withHeader("Content-Type", "application/hal+json")
            .withBody(TestConstatns.EXPECTED_BOOKS_PAGE)));

    Page<Book> page = bookClient.findByTerm("", PageRequest.of(0, 8, "title"));
    assertNotNull(page);
    assertEquals("Design Patterns com Java", page.getContent().iterator().next().getTitle());
  }

  @Test
  void givenABook_thenSaveTheBook() {
    wireMockServer.stubFor(post(urlEqualTo("/v1/books/"))
        .willReturn(aResponse()
            .withStatus(HttpStatus.CREATED.value())
            .withHeader("Content-Type", "application/hal+json")
            .withBody(TestConstatns.EXPECTED_BOOK_CREATED)));

    Book book = new Book().id(1).title("Effective Java").subtitle("Programming Language Guide")
        .author("Joshua Bloch").publisher("Addison-Wesley").pages(252);
    Book savedBook = bookClient.create(book);
    assertNotNull(savedBook);
    assertEquals(book.getId(), savedBook.getId());
  }
}