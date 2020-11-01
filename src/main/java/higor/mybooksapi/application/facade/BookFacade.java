package higor.mybooksapi.application.facade;

import higor.mybooksapi.application.facade.dto.BookDto;
import higor.mybooksapi.domain.book.Book;
import higor.mybooksapi.domain.book.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BookFacade {
  private final BookRepository repository;

  public BookFacade(BookRepository repository) {
    this.repository = repository;
  }

  public Page<BookDto> list(String filter, int page, int size, Sort.Direction direction, String sortBy) {
    Page<Book> books = repository.findAll(PageRequest.of(0, 10, direction, sortBy));
//    Page<Book> books = repository.findAll(PageRequest.of(page, size));
    Page<BookDto> booksDto = new PageImpl<>(new ArrayList<>());
    return booksDto;
  }

  public long create(BookDto bookDto) {
    return 0;
  }
}
