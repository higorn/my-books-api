package higor.mybooks.infra.remotedata.user;

import higor.mybooks.domain.user.User;
import higor.mybooks.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRemoteRepositoryTest {
  @Mock
  private UserClient userClient;

  @Test
  void givenAnEmail_whenTheAccountExists_thenReturnsTheUser() {
    String email = "nicanor@mybooks.com";
    when(userClient.findByEmail(anyString())).thenReturn(Optional.of(new User().email(email)));

    UserRepository repository = new UserRemoteRepository(userClient);
    Optional<User> userOpt = repository.findByEmail(email);

    assertTrue(userOpt.isPresent());
  }
}