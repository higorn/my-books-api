package higor.mybooks.domain.userbook;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserBookRepository {
  Page<UserBook> findByUserId(Integer userId, Pageable pageable);
  <S extends UserBook> S save(S entity);
}
