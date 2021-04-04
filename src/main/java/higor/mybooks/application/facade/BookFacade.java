package higor.mybooks.application.facade;

import higor.mybooks.api.dto.BookDto;
import higor.mybooks.application.mapper.BookMapper;
import higor.mybooks.domain.book.Book;
import higor.mybooks.domain.book.BookRepository;
import higor.mybooks.domain.book.BookRepository2;
import higor.mybooks.domain.page.MyPage;
import higor.mybooks.domain.page.MyPageRequest;
import higor.mybooks.domain.user.User;
import higor.mybooks.domain.userbook.UserBook;
import higor.mybooks.domain.userbook.UserBookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static higor.mybooks.application.mapper.BookMapper.toBook;

@Component
public class BookFacade {
  private final BookRepository     bookRepository;
  private final BookRepository2    bookRepository2;
  private final UserBookRepository userBookRepository;

  public BookFacade(BookRepository bookRepository, BookRepository2 bookRepository2, UserBookRepository userBookRepository) {
    this.bookRepository = bookRepository;
    this.bookRepository2 = bookRepository2;
    this.userBookRepository = userBookRepository;
  }

  public Page<BookDto> search(String term, int page, int size, Sort.Direction direction, String sortBy) {
    term = Optional.ofNullable(term).orElse("");
    return bookRepository.findByTerm(term, PageRequest.of(page, size, direction, sortBy)).map(BookMapper::toBookDto);
  }

  public Integer create(BookDto bookDto, User user) {
    Book book = bookRepository.save(toBook(bookDto));
    userBookRepository.save(new UserBook()
        .user("/v1/users/" + user.getId())
        .book("/v1/books/" + book.getId())
        .read(bookDto.read));
    return book.getId();
  }

  public MyPage<BookDto> search2(String term, int page, int size, String sort, MyPageRequest.SortDirection direction) {
    return bookRepository2.findByTerm(Optional.ofNullable(term).orElse(""),
        MyPageRequest.of(page, size, sort, direction)).map(BookMapper::toBookDto);
  }
}
