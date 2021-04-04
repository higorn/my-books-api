package higor.mybooks.infra.remotedata.userbook;

import higor.mybooks.domain.userbook.UserBook;
import higor.mybooks.domain.userbook.UserBookRepository;
import higor.mybooks.infra.remotedata.AbstractRemoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class UserBookRemoteRepository extends AbstractRemoteRepository<UserBook, Integer> implements UserBookRepository {

  protected UserBookRemoteRepository(UserBookClient userBookClient) {
    super(userBookClient);
  }

  @Override
  public Page<UserBook> findByUserId(Integer userId, Pageable pageable) {
    return null;
  }
}
