package higor.mybooks.application.facade;

import higor.mybooks.api.dto.BookDto;
import higor.mybooks.application.mapper.BookMapper;
import higor.mybooks.domain.book.Book;
import higor.mybooks.domain.book.BookRepository;
import higor.mybooks.domain.page.Page;
import higor.mybooks.domain.page.PageRequest;
import higor.mybooks.domain.user.User;
import higor.mybooks.domain.userbook.UserBook;
import higor.mybooks.domain.userbook.UserBookRepository;
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

  public Page<BookDto> search(String term, int page, int size, String sort, PageRequest.SortDirection direction) {
    term = Optional.ofNullable(term).orElse("");
    return bookRepository.findByTerm(term, PageRequest.of(page, size, sort, direction)).map(BookMapper::toBookDto);
  }

  public Integer create(BookDto bookDto, User user) {
    Book book = bookRepository.save(toBook(bookDto));
    userBookRepository.save(new UserBook()
        .user("/v1/users/" + user.getId())
        .book("/v1/books/" + book.getId())
        .read(bookDto.read));
    return book.getId();
  }
}
