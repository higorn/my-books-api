package higor.mybooks.infra.remotedata;

import higor.mybooks.domain.book.Book;
import higor.mybooks.domain.book.BookClient;
import higor.mybooks.domain.book.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookRemoteRepositoryTest {

  @Mock
  private BookClient bookClient;
  private BookRepository bookRepository;

  @BeforeEach
  void setUp() {
    bookRepository = new BookRemoteRepository(bookClient);
  }

  @Test
  void whenFindByTerm_thenReturnsAPageOfBooks() {
    List<EntityModel<Book>> books = new ArrayList<>();
    books.add(EntityModel.of(new Book().id(1).title("Effective Java").subtitle("Programming Language Guide")
        .author("Joshua Bloch").publishingCompaty("Addison-Wesley").pages(252),
        Link.of("http://localhost/v1/books/1", "self")));
    books.add(EntityModel.of(new Book().id(2).title("Effective Java2").subtitle("Programming Language Guide2")
        .author("Joshua Bloch2").publishingCompaty("Addison-Wesley2").pages(152),
        Link.of("http://localhost/v1/books/2", "self")));
    when(bookClient.findByTerm(any(), any(Pageable.class)))
        .thenReturn(PagedModel.of(books, new PagedModel.PageMetadata(10, 0, books.size())));

    Page<Book> booksPage = bookRepository.findByTerm(null, PageRequest.of(0, 10, Sort.Direction.ASC, "title"));

    assertBooks(books, booksPage);
    verify(bookClient).findByTerm(any(), any(Pageable.class));
  }

  @Test
  void whenSave_thenReturnsTheNewCreatedBook() {
    Book book = new Book().id(1).title("Effective Java").subtitle("Programming Language Guide").author("Joshua Bloch")
        .publishingCompaty("Addison-Wesley").pages(252);
    when(bookClient.create(book)).thenReturn(EntityModel.of(book, Link.of("http://localhost/v1/books/1", "self")));

    Book bookSaved = bookRepository.save(book);

    assertNotNull(bookSaved);
    assertBook(book, bookSaved);
    verify(bookClient).create(book);
  }

  @Test
  void whenUpdateBookUser_thenUpdate() {
    bookRepository.updateBookUser(1, 3);

    verify(bookClient).updateBookUsers(1, "/v1/users/3");
  }

  private void assertBooks(List<EntityModel<Book>> expectedBooks, Page<Book> booksPage) {
    assertNotNull(booksPage);
    assertEquals(expectedBooks.size(), booksPage.getTotalElements());
    for (int i = 0; i < expectedBooks.size(); i++) {
      Book expectedBook = expectedBooks.get(i).getContent();
      Book book = booksPage.getContent().get(i);
      assertBook(expectedBook, book);
    }
  }

  private void assertBook(Book expectedBook, Book book) {
    assertNotNull(book.getId());
    assertEquals(expectedBook.getId(), book.getId());
    assertEquals(expectedBook.getTitle(), book.getTitle());
    assertEquals(expectedBook.getSubtitle(), book.getSubtitle());
    assertEquals(expectedBook.getAuthor(), book.getAuthor());
    assertEquals(expectedBook.getPublishingCompany(), book.getPublishingCompany());
    assertEquals(expectedBook.getPages(), book.getPages());
  }
}