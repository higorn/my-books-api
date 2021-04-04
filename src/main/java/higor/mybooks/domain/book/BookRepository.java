package higor.mybooks.domain.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepository {
  Page<Book> findByTitleContainingIgnoreCaseOrSubtitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrPublishingCompanyContainingIgnoreCase(
      String title, String subtitle, String author, String publishingCompany, Pageable pageable);
  Page<Book> findByTerm(String term, Pageable pageable);

  <S extends Book> S save(S entity);
  void updateBookUser(Integer bookId, Integer userId);
}
