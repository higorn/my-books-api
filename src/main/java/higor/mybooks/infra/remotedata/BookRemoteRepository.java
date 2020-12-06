package higor.mybooks.infra.remotedata;

import higor.mybooks.domain.book.Book;
import higor.mybooks.domain.book.BookClient;
import higor.mybooks.domain.book.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
      String title, String subtitle, String author, String publishingCompany, Pageable pageable) {
    return null;
  }

  @Override
  public Page<Book> findByTerm(String term, Pageable pageable) {
    return toEntityPage(bookClient.findByTerm(term, pageable), pageable.getSort());
  }

  @Override
  public void updateBookUser(Integer bookId, Integer userId) {
    ResponseEntity<Void> response = bookClient.updateBookUsers(bookId, "/v1/users/" + userId);
    // TODO: throw exception when response status different of 204
  }
}
