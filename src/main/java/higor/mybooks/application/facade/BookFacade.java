package higor.mybooks.application.facade;

import higor.mybooks.domain.book.Book;
import higor.mybooks.domain.book.BookRepository;
import higor.mybooks.domain.user.User;
import higor.mybooks.domain.userbook.UserBook;
import higor.mybooks.domain.userbook.UserBookRepository;
import higor.mybooks.api.dto.BookDto;
import higor.mybooks.application.mapper.BookMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static higor.mybooks.application.mapper.BookMapper.toBook;

@Component
public class BookFacade {
  private final BookRepository     bookRepository;
  private final UserBookRepository userBookRepository;

  public BookFacade(BookRepository bookRepository, UserBookRepository userBookRepository) {
    this.bookRepository = bookRepository;
    this.userBookRepository = userBookRepository;
  }

  public Page<BookDto> list(String filter, int page, int size, Sort.Direction direction, String sortBy) {
    filter = Optional.ofNullable(filter).orElse("");
    return bookRepository.findByTitleContainingIgnoreCaseOrSubtitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrPublishingCompanyContainingIgnoreCase(
        filter, filter, filter, filter, PageRequest.of(page, size, direction, sortBy)).map(BookMapper::toBookDto);
  }

  public Integer create(BookDto bookDto, User user) {
    Book book = bookRepository.save(toBook(bookDto));
    userBookRepository.save(new UserBook().user(user).book(book).read(bookDto.read));
    return book.getId();
  }
}
