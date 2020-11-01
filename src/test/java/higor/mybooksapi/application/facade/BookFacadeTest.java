package higor.mybooksapi.application.facade;

import higor.mybooksapi.application.facade.dto.BookDto;
import higor.mybooksapi.domain.book.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookFacadeTest {

  @Mock
  private BookRepository repository;

  private BookFacade facade;

  @BeforeEach
  void setUp() {
    facade = new BookFacade(repository);
  }

  @Test
  void givenNoFilter_whenList_thenReturnAPageOfBooks() {
    Page<BookDto> booksPage = facade.list(null, 0, 10, Sort.Direction.ASC, "tittle");

    assertNotNull(booksPage);
    verify(repository).findAll(any(Pageable.class));
  }
}