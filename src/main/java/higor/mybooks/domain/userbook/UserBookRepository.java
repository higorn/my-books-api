package higor.mybooks.domain.userbook;

import higor.mybooks.domain.page.Page;
import higor.mybooks.domain.page.PageRequest;

public interface UserBookRepository {
  Page<UserBook> findByUserId(Integer userId, PageRequest pageRequest);
  <S extends UserBook> S save(S entity);
}
