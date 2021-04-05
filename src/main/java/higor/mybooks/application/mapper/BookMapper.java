package higor.mybooks.application.mapper;

import higor.mybooks.api.dto.BookDto;
import higor.mybooks.domain.book.Book;

public class BookMapper {
  private BookMapper() {
  }

  public static Book toBook(BookDto bookDto) {
    return new Book().title(bookDto.title).subtitle(bookDto.subtitle).author(bookDto.author)
        .publisher(bookDto.publishingCompany).pages(bookDto.pages);
  }

  public static BookDto toBookDto(Book book, boolean isRead) {
    BookDto bookDto = toBookDto(book);
    bookDto.read = isRead;
    return bookDto;
  }

  public static BookDto toBookDto(Book book) {
    return new BookDto().id(book.getId()).title(book.getTitle()).subtitle(book.getSubtitle())
        .author(book.getAuthor()).publishingCompany(book.getPublisher()).pages(book.getPages());
  }
}
