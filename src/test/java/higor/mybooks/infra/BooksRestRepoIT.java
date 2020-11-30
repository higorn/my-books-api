package higor.mybooks.infra;

import higor.mybooks.domain.book.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class BooksRestRepoIT {

  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private BooksClient booksClient;
  @MockBean
  private JwtDecoder jwtDecoder;

  @Test
  void shouldGetBooks() {
    PagedModel<Book> books = booksClient.getBooks(PageRequest.of(0, 10, Sort.Direction.ASC, "title"));
    assertFalse(books.getContent().isEmpty());
//    Book book = booksClient.getBook(1);
//    System.out.println(book);
//    Page<Book> booksPage = restTemplate.getForObject("http://localhost:8081/v1/books", Page.class);
/*
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
    messageConverter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON, MediaType.APPLICATION_JSON));
    messageConverter.setObjectMapper(mapper);
    restTemplate.setMessageConverters(Collections.singletonList(messageConverter));
    ResponseEntity<PagedModel<Book>> responseEntity = restTemplate
        .exchange("http://localhost:8081/v1/books", HttpMethod.GET, HttpEntity.EMPTY,
            new ParameterizedTypeReference<PagedModel<Book>>() {
            });
    //    String booksPage = restTemplate.getForObject("http://localhost:8081/v1/books", String.class);
    System.out.println(responseEntity);
    assertNotNull(responseEntity);
*/
  }

}
