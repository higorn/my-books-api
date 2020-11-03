package higor.mybooksapi.application.facade;

import higor.mybooksapi.application.facade.dto.BookDto;
import higor.mybooksapi.domain.book.Book;
import higor.mybooksapi.domain.book.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BookFacade {
  private final BookRepository repository;

  public BookFacade(BookRepository repository) {
    this.repository = repository;
  }

  public Page<BookDto> list(String filter, int page, int size, Sort.Direction direction, String sortBy) {
    return repository.findAll(PageRequest.of(page, size, direction, sortBy)).map(this::mapToDto);
  }

  private BookDto mapToDto(Book book) {
    BookDto bookDto = new BookDto();
    bookDto.id = book.getId();
    bookDto.title = book.getTitle();
    bookDto.subtitle = book.getSubtitle();
    bookDto.author = book.getAuthor();
    bookDto.publishingCompany = book.getPublishingCompany();
    bookDto.pages = book.getPages();
    bookDto.read = book.isRead();
    return bookDto;
  }

  public long create(BookDto bookDto) {
    return 0;
  }
}
