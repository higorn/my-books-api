package higor.mybooksapi.domain.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookRepositoryTest {

  @Autowired
  private BookRepository repository;

  @Test
  void whenSaved_thenFindsByTitle() {
    Book book = new Book();
    book.setTitle("Effective Java");
    book.setAuthor("Joshua Bloch");
    Book savedBook = repository.save(book);
    assertNotNull(savedBook.getId());
  }

  // TODO: list books with filter

}