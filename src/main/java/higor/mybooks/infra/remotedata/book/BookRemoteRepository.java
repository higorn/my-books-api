package higor.mybooks.infra.remotedata.book;

import higor.mybooks.domain.book.Book;
import higor.mybooks.domain.book.BookRepository;
import higor.mybooks.domain.page.Page;
import higor.mybooks.domain.page.PageRequest;
import higor.mybooks.infra.remotedata.AbstractRemoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public class BookRemoteRepository extends AbstractRemoteRepository<Book, Integer> implements BookRepository {
  private final BookClient bookClient;

  public BookRemoteRepository(BookClient bookClient) {
    super(bookClient);
    this.bookClient = bookClient;
  }

  @Override
  public Page<Book> findByTitleContainingIgnoreCaseOrSubtitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrPublishingCompanyContainingIgnoreCase(
      String title, String subtitle, String author, String publishingCompany, PageRequest pageRequest) {
    return null;
  }

  @Override
  public Page<Book> findByTerm(String term, PageRequest pageRequest) {
    return bookClient.findByTerm(term, pageRequest);
  }

  @Override
  public void updateBookUser(Integer bookId, Integer userId) {
    ResponseEntity<Void> response = bookClient.updateBookUsers(bookId, "/v1/users/" + userId);
    // TODO: throw exception when response status different of 204
  }
}
