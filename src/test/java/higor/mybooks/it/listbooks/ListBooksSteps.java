package higor.mybooks.it.listbooks;

import higor.mybooks.api.dto.BookDto;
import higor.mybooks.application.facade.BookFacade;
import higor.mybooks.domain.book.Book;
import higor.mybooks.domain.book.BookClient;
import higor.mybooks.domain.user.User;
import higor.mybooks.domain.userbook.UserBookRepository;
import higor.mybooks.infra.remotedata.BookRemoteRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = {
    ListBooksSteps.BookClientProvider.class,
    BookFacade.class, BookRemoteRepository.class
})
@DirtiesContext
public class ListBooksSteps {

  @TestConfiguration
  public static class BookClientProvider {
    @Bean
    public BookClient bookClient() {
      return new BookClient() {
        @Override
        public PagedModel<Book> findAll(Pageable page) {
          return null;
        }

        @Override
        public EntityModel<Book> findById(int id) {
          return null;
        }

        @Override
        public PagedModel<EntityModel<Book>> findByTerm(String term, Pageable page) {
          return PagedModel.of(books, new PagedModel.PageMetadata(10, 0, books.size()));
        }

        @Override
        public PagedModel<EntityModel<User>> findBookUsers(int id) {
          return null;
        }

        @Override
        public ResponseEntity<Void> updateBookUsers(int id, String userUris) {
          return null;
        }

        @Override
        public EntityModel<Book> create(Book book) {
          return null;
        }
      };
    }
  }

  @Autowired
  private BookFacade         bookFacade;
  @Autowired
  private BookClient         bookClient;
  @MockBean
  private UserBookRepository userBookRepository;
  @MockBean
  private JwtDecoder         jwtDecoder;
  private Page<BookDto>      booksPage;
  private static List<EntityModel<Book>> books;

  @Given("that I have books in the database")
  public void that_I_have_books_in_the_database(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    books = new ArrayList<>();
    AtomicInteger i = new AtomicInteger();
    rows.forEach(row -> {
      Book book = new Book()
          .id(i.getAndIncrement())
          .title(row.get("title"))
          .subtitle(row.get("subtitle"))
          .author(row.get("author"))
          .publishingCompaty(row.get("publishingCompany"));
      books.add(EntityModel.of(book, Link.of("http://localhost/v1/books/" + i.get(), "self")));
    });
  }

  @When("I ask for the book list")
  public void i_ask_for_the_book_list() {
    booksPage = bookFacade.search("", 0, 10, Sort.Direction.ASC, "title");
  }

  @Then("I receive a list of books of size {int}")
  public void i_receive_a_list_of_books_of_size(Integer size) {
    assertEquals(size, booksPage.getContent().size());
  }
}
