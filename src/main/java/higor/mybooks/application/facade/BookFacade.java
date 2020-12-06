package higor.mybooks.application.facade;

import higor.mybooks.api.dto.BookDto;
import higor.mybooks.application.mapper.BookMapper;
import higor.mybooks.domain.book.Book;
import higor.mybooks.domain.book.BookRepository;
import higor.mybooks.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static higor.mybooks.application.mapper.BookMapper.toBook;

@Component
public class BookFacade {
  private final BookRepository     bookRepository;

  public BookFacade(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public Page<BookDto> search(String term, int page, int size, Sort.Direction direction, String sortBy) {
    term = Optional.ofNullable(term).orElse("");
    return bookRepository.findByTerm(term, PageRequest.of(page, size, direction, sortBy)).map(BookMapper::toBookDto);
  }

  public Integer create(BookDto bookDto, User user) {
    Book book = bookRepository.save(toBook(bookDto));
    bookRepository.updateBookUser(book.getId(), user.getId());
    return book.getId();
  }
}
