package higor.mybooks.infra.remotedata.book;

import higor.mybooks.domain.book.Book;
import higor.mybooks.domain.book.BookRepository;
import higor.mybooks.domain.page.Page;
import higor.mybooks.domain.page.PageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookRemoteRepositoryTest {

  @Mock
  private BookClient      bookClient;
  private BookRepository  bookRepository;

  @BeforeEach
  void setUp() {
    bookRepository = new BookRemoteRepository(bookClient);
  }

  @Test
  void whenFindByTerm_thenReturnsAPageOfBooks() {
    List<Book> books = new ArrayList<>();
    books.add(new Book().id(1).title("Effective Java").subtitle("Programming Language Guide")
        .author("Joshua Bloch").publisher("Addison-Wesley").pages(252));
    books.add(new Book().id(2).title("Effective Java2").subtitle("Programming Language Guide2")
        .author("Joshua Bloch2").publisher("Addison-Wesley2").pages(152));
    when(bookClient.findByTerm(any(), any(PageRequest.class)))
        .thenReturn(Page.of(books, Page.Metadata.of(0, 10, books.size())));

    Page<Book> booksPage = bookRepository.findByTerm(null, PageRequest.of(0, 10, "title", PageRequest.SortDirection.ASC));

    assertBooks(books, booksPage);
    verify(bookClient).findByTerm(any(), any(PageRequest.class));
  }

  @Test
  void whenSave_thenReturnsTheNewCreatedBook() {
    Book book = new Book().id(1).title("Effective Java").subtitle("Programming Language Guide").author("Joshua Bloch")
        .publisher("Addison-Wesley").pages(252);
    when(bookClient.create(book)).thenReturn(book);

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

  private void assertBooks(List<Book> expectedBooks, Page<Book> booksPage) {
    assertNotNull(booksPage);
    assertEquals(expectedBooks.size(), booksPage.getTotalElements());
    Iterator<Book> it1 = expectedBooks.iterator();
    Iterator<Book> it2 = booksPage.getContent().iterator();
    for (Book b1 = it1.next(), b2 = it2.next(); it1.hasNext() && it2.hasNext(); b1 = it1.next(), it2.next())
      assertBook(b1, b2);
  }

  private void assertBook(Book expectedBook, Book book) {
    assertNotNull(book.getId());
    assertEquals(expectedBook.getId(), book.getId());
    assertEquals(expectedBook.getTitle(), book.getTitle());
    assertEquals(expectedBook.getSubtitle(), book.getSubtitle());
    assertEquals(expectedBook.getAuthor(), book.getAuthor());
    assertEquals(expectedBook.getPublisher(), book.getPublisher());
    assertEquals(expectedBook.getPages(), book.getPages());
  }
}