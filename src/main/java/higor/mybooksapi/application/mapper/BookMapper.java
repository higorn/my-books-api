package higor.mybooksapi.application.mapper;

import higor.mybooksapi.application.dto.BookDto;
import higor.mybooksapi.domain.book.Book;

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
    book.setRead(bookDto.read);
    return book;
  }
}
