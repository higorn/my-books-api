package higor.mybooks.infra.remotedata.user;

import higor.mybooks.domain.user.User;
import higor.mybooks.domain.user.UserRepository;
import higor.mybooks.infra.remotedata.AbstractRemoteRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRemoteRepository extends AbstractRemoteRepository<User, Integer> implements UserRepository {
  private final UserClient userClient;

  protected UserRemoteRepository(UserClient userClient) {
    super(userClient);
    this.userClient = userClient;
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userClient.findByEmail(email);
  }
}
