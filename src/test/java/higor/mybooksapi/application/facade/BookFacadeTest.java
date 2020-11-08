package higor.mybooksapi.application.facade;

import higor.mybooksapi.application.dto.BookDto;
import higor.mybooksapi.domain.book.Book;
import higor.mybooksapi.domain.book.BookRepository;
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

import static higor.mybooksapi.application.mapper.BookMapper.toBook;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookFacadeTest {

  @Mock
  private BookRepository repository;

  private BookFacade facade;

  @BeforeEach
  void setUp() {
    facade = new BookFacade(repository);
  }

  @Test
  void givenNoFilter_whenList_thenReturnAPageOfBooks() {
    List<Book> books = new ArrayList<>();
    books.add(createBook(1, "Effective Java", "Programming Language Guide",
        "Joshua Bloch", "Addison-Wesley", 252, true));
    books.add(createBook(2, "Effective Java2", "Programming Language Guide2",
        "Joshua Bloch2", "Addison-Wesley2", 152, false));
    when(repository.findByTitleContainingIgnoreCaseOrSubtitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrPublishingCompanyContainingIgnoreCase(
        any(), any(), any(), any(), any(Pageable.class))).thenReturn(new PageImpl<>(books));

    Page<BookDto> booksPage = facade.list(null, 0, 10, Sort.Direction.ASC, "title");

    assertBooks(books, booksPage);
    verify(repository).findByTitleContainingIgnoreCaseOrSubtitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrPublishingCompanyContainingIgnoreCase(
        any(), any(), any(), any(), any(Pageable.class));
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

  private void assertBookCreation(int i, boolean read) {
    BookDto bookDto = createBookDto("Effective Java", "Programming Language Guide",
        "Joshua Bloch", "Addison-Wesley", 252, read);
    Book book = toBook(bookDto);
    doAnswer(invocation -> {
      Book b = invocation.getArgument(0, Book.class);
      b.setId(i);
      assertEquals(book.getTitle(), b.getTitle());
      assertNotNull(b.getSubtitle());
      assertEquals(book.getSubtitle(), b.getSubtitle());
      assertEquals(book.getAuthor(), b.getAuthor());
      assertNotNull(b.getPublishingCompany());
      assertEquals(book.getPublishingCompany(), b.getPublishingCompany());
      assertNotNull(b.getPages());
      assertEquals(book.getPages(), b.getPages());
      assertEquals(read, b.isRead());
      return b;
    }).when(repository).save(any(Book.class));

    // TODO: exchange to the create with user
    long bookId = facade.create(bookDto);

    assertEquals(i, bookId);
    verify(repository).save(any(Book.class));
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
      assertEquals(book.getPublishingCompany(), bookDto.publishingCompany);
      assertEquals(book.getPages(), bookDto.pages);
      assertEquals(book.isRead(), bookDto.read);
    }
  }

  private Book createBook(Integer id, String title, String subTitle, String author, String publishingCompany,
      Integer pages, boolean read) {
    Book book = new Book();
    book.setId(id);
    book.setTitle(title);
    book.setSubtitle(subTitle);
    book.setAuthor(author);
    book.setPublishingCompany(publishingCompany);
    book.setPages(pages);
    book.setRead(read);
    return book;
  }

  private BookDto createBookDto(String title, String subTitle, String author, String publishingCompany,
      Integer pages, boolean read) {
    BookDto book = new BookDto();
    book.title = title;
    book.subtitle = subTitle;
    book.author = author;
    book.publishingCompany = publishingCompany;
    book.pages = pages;
    book.read = read;
    return book;
  }
}