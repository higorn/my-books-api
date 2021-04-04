package higor.mybooks.domain.book;

import higor.mybooks.domain.page.MyPage;
import higor.mybooks.domain.page.MyPageRequest;

public interface BookRepository2 {
  MyPage<Book> findByTerm(String term, MyPageRequest pageRequest);
}
