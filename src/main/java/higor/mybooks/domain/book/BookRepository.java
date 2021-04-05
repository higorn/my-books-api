package higor.mybooks.domain.book;

import higor.mybooks.domain.page.Page;
import higor.mybooks.domain.page.PageRequest;

public interface BookRepository {
  Page<Book> findByTitleContainingIgnoreCaseOrSubtitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrPublishingCompanyContainingIgnoreCase(
      String title, String subtitle, String author, String publishingCompany, PageRequest pageRequest);
  Page<Book> findByTerm(String term, PageRequest pageRequest);

  <S extends Book> S save(S entity);
  void updateBookUser(Integer bookId, Integer userId);
}
