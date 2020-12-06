package higor.mybooks.infra.remotedata;

import higor.mybooks.domain.userbook.UserBook;
import higor.mybooks.domain.userbook.UserBookClient;
import higor.mybooks.domain.userbook.UserBookRepository;
import higor.mybooks.domain.userbook.UserBookSv;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Repository;

@Repository
public class UserBookRemoteRepository extends AbstractRemoteRepository<UserBook, Integer>
    implements UserBookRepository {

  private final UserBookClient userBookClient;

  public UserBookRemoteRepository(UserBookClient userBookClient) {
    super(userBookClient);
    this.userBookClient = userBookClient;
  }

  @Override
  public Page<UserBook> findByUserId(Integer userId, Pageable pageable) {
    return toEntityPage(userBookClient.findByUserId(userId, pageable), pageable.getSort());
  }

  @Override
  public UserBook save(UserBook entity) {

    UserBookSv entity1 = new UserBookSv(entity);
    return toEntity2(userBookClient.save(entity1));
  }

  protected UserBook toEntity2(EntityModel<UserBookSv> entityModel) {
    String href = entityModel.getLink("self").get().getHref();
    Integer id = Integer.valueOf(href.substring(href.lastIndexOf("/") + 1));
    return new UserBook().id(id);
//    return entityModel.getContent().id((ID) id);
  }
}
