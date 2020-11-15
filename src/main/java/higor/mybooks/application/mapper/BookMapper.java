package higor.mybooks.application.mapper;

import higor.mybooks.api.dto.BookDto;
import higor.mybooks.domain.book.Book;

public class BookMapper {
  private BookMapper() {
  }

  public static Book toBook(BookDto bookDto) {
    Book book = new Book();
    book.setTitle(bookDto.title);
    book.setSubtitle(bookDto.subtitle);
    book.setAuthor(bookDto.author);
    book.setPublishingCompany(bookDto.publishingCompany);
    book.setPages(bookDto.pages);
    return book;
  }

  public static BookDto toBookDto(Book book, boolean isRead) {
    BookDto bookDto = toBookDto(book);
    bookDto.read = isRead;
    return bookDto;
  }

  public static BookDto toBookDto(Book book) {
    BookDto bookDto = new BookDto();
    bookDto.id = book.getId();
    bookDto.title = book.getTitle();
    bookDto.subtitle = book.getSubtitle();
    bookDto.author = book.getAuthor();
    bookDto.publishingCompany = book.getPublishingCompany();
    bookDto.pages = book.getPages();
    return bookDto;
  }
}
