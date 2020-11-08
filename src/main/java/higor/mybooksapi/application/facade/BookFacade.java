package higor.mybooksapi.application.facade;

import higor.mybooksapi.application.dto.BookDto;
import higor.mybooksapi.application.mapper.BookMapper;
import higor.mybooksapi.domain.book.BookRepository;
import higor.mybooksapi.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static higor.mybooksapi.application.mapper.BookMapper.toBook;

@Component
public class BookFacade {
  private final BookRepository repository;

  public BookFacade(BookRepository repository) {
    this.repository = repository;
  }

  public Page<BookDto> list(String filter, int page, int size, Sort.Direction direction, String sortBy) {
    filter = Optional.ofNullable(filter).orElse("");
    return repository.findByTitleContainingIgnoreCaseOrSubtitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrPublishingCompanyContainingIgnoreCase(
        filter, filter, filter, filter, PageRequest.of(page, size, direction, sortBy)).map(BookMapper::toBookDto);
  }

  public Integer create(BookDto bookDto) {
    return repository.save(toBook(bookDto)).getId();
  }

  public Integer create(BookDto bookDto, User user) {
    return null;
  }
}
