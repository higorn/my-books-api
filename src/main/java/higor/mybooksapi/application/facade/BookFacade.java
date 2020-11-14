package higor.mybooksapi.application.facade;

import higor.mybooksapi.api.dto.BookDto;
import higor.mybooksapi.application.mapper.BookMapper;
import higor.mybooksapi.domain.book.Book;
import higor.mybooksapi.domain.book.BookRepository;
import higor.mybooksapi.domain.user.User;
import higor.mybooksapi.domain.userbook.UserBook;
import higor.mybooksapi.domain.userbook.UserBookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static higor.mybooksapi.application.mapper.BookMapper.toBook;

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
    userBookRepository.save(newUserBook(book, user, bookDto.read));
    return book.getId();
  }

  private UserBook newUserBook(Book book, User user, boolean read) {
    UserBook userBook = new UserBook();
    userBook.setUser(user);
    userBook.setBook(book);
    userBook.setRead(read);
    return userBook;
  }
}
