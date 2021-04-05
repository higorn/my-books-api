package higor.mybooks.domain.user;

import java.util.Optional;

public interface UserRepository {
  Optional<User> findByEmail(String email);
  <S extends User> S save(S entity);
}
