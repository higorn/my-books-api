package higor.mybooks.application.facade;

import higor.mybooks.api.dto.BookDto;
import higor.mybooks.domain.book.Book;
import higor.mybooks.domain.book.BookRepository;
import higor.mybooks.domain.book.BookRepository2;
import higor.mybooks.domain.page.MyPage;
import higor.mybooks.domain.page.MyPageRequest;
import higor.mybooks.domain.user.User;
import higor.mybooks.domain.userbook.UserBook;
import higor.mybooks.domain.userbook.UserBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static higor.mybooks.application.mapper.BookMapper.toBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookFacadeTest {

  @Mock
  private BookRepository     bookRepository;
  @Mock
  private BookRepository2    bookRepository2;
  @Mock
  private UserBookRepository userBookRepository;

  private BookFacade bookFacade;

  @BeforeEach
  void setUp() {
    bookFacade = new BookFacade(bookRepository, bookRepository2, userBookRepository);
  }

  @Test
  void givenNoFilter_thenReturnAPageOfBooks() {
    List<Book> books = new ArrayList<>();
    books.add(new Book().id(1).title("Effective Java").subtitle("Programming Language Guide")
        .author("Joshua Bloch").publisher("Addison-Wesley").pages(252));
    books.add(new Book().id(2).title("Effective Java2").subtitle("Programming Language Guide2")
        .author("Joshua Bloch2").publisher("Addison-Wesley2").pages(152));
    when(bookRepository.findByTerm(any(), any(Pageable.class))).thenReturn(new PageImpl<>(books));

    Page<BookDto> booksPage = bookFacade.search(null, 0, 10, Sort.Direction.ASC, "title");

    assertBooks(books, booksPage);
    verify(bookRepository).findByTerm(any(), any(Pageable.class));
  }

  @Test
  void givenNoFilter_thenReturnAPageOfBooks2() {
    List<Book> books = new ArrayList<>();
    books.add(new Book().id(1).title("Effective Java").subtitle("Programming Language Guide")
        .author("Joshua Bloch").publisher("Addison-Wesley").pages(252));
    books.add(new Book().id(2).title("Effective Java2").subtitle("Programming Language Guide2")
        .author("Joshua Bloch2").publisher("Addison-Wesley2").pages(152));
    when(bookRepository2.findByTerm(any(), any(MyPageRequest.class))).thenReturn(MyPage.of(books));

    MyPage<BookDto> booksPage = bookFacade.search2(null, 0, 10, "title", MyPageRequest.SortDirection.ASC);

    assertBooks2(books, booksPage);
    verify(bookRepository2).findByTerm(any(), any(MyPageRequest.class));
  }

  @Test
  void whenCreateABook_thenReturnsItsId() {
    assertBookCreation(1, true);
  }

  @Test
  void whenCreateAnotherBook_thenReturnsItsId() {
    assertBookCreation(2, false);
  }

  // TODO: get book deatil
  // TODO: set book as reed
  // TODO: when book is read increase user points

  private void assertBookCreation(int expectedBookId, boolean read) {
    // Given
    BookDto bookDto = new BookDto().title("Effective Java").subtitle("Programming Language Guide")
        .author("Joshua Bloch").publishingCompany("Addison-Wesley").pages(252).read(read);
    doAnswer(invocation -> getExpectedBook(expectedBookId, bookDto, invocation.getArgument(0, Book.class)))
        .when(bookRepository).save(any(Book.class));

    // When
    long bookId = bookFacade.create(bookDto, new User().id(1));

    // Then
    assertEquals(expectedBookId, bookId);
    verify(bookRepository).save(any(Book.class));
    verify(userBookRepository).save(any(UserBook.class));
  }

  private void assertBooks(List<Book> books, Page<BookDto> booksPage) {
    assertNotNull(booksPage);
    assertEquals(books.size(), booksPage.getTotalElements());
    for (int i = 0; i < books.size(); i++) {
      Book book = books.get(i);
      BookDto bookDto = booksPage.getContent().get(i);
      assertEquals(book.getId(), bookDto.id);
      assertEquals(book.getTitle(), bookDto.title);
      assertEquals(book.getSubtitle(), bookDto.subtitle);
      assertEquals(book.getAuthor(), bookDto.author);
      assertEquals(book.getPublisher(), bookDto.publishingCompany);
      assertEquals(book.getPages(), bookDto.pages);
    }
  }

  private void assertBooks2(List<Book> books, MyPage<BookDto> booksPage) {
    assertNotNull(booksPage);
    assertEquals(books.size(), booksPage.getTotalElements());
    for (int i = 0; i < books.size(); i++) {
      Book book = books.get(i);
      BookDto bookDto = booksPage.getContent().get(i);
      assertEquals(book.getId(), bookDto.id);
      assertEquals(book.getTitle(), bookDto.title);
      assertEquals(book.getSubtitle(), bookDto.subtitle);
      assertEquals(book.getAuthor(), bookDto.author);
      assertEquals(book.getPublisher(), bookDto.publishingCompany);
      assertEquals(book.getPages(), bookDto.pages);
    }
  }

  private Book getExpectedBook(int i, BookDto bookDto, Book b) {
    Book expectedBook = toBook(bookDto);
    b.setId(i);
    assertEquals(expectedBook.getTitle(), b.getTitle());
    assertNotNull(b.getSubtitle());
    assertEquals(expectedBook.getSubtitle(), b.getSubtitle());
    assertEquals(expectedBook.getAuthor(), b.getAuthor());
    assertNotNull(b.getPublisher());
    assertEquals(expectedBook.getPublisher(), b.getPublisher());
    assertNotNull(b.getPages());
    assertEquals(expectedBook.getPages(), b.getPages());
    return b;
  }
}