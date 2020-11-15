package higor.mybooks.application.facade;

import higor.mybooks.application.facade.UserFacade;
import higor.mybooks.domain.user.User;
import higor.mybooks.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

  @Mock
  private UserRepository userRepository;

  private UserFacade userFacade;

  @BeforeEach
  void setUp() {
    userFacade = new UserFacade(userRepository);
  }

  @Test
  void givenTheEmailFromTheAuthentication_whenFoundInTheDB_thenReturnsTheUser() {
    // Given
    String stubEmail = "nicanor@email.com";
    User expectedUser = new User();
    expectedUser.setEmail(stubEmail);
    when(userRepository.findByEmail(stubEmail)).thenReturn(of(expectedUser));
    SecurityContextHolder.getContext().setAuthentication(new StubAuthentication(stubEmail));

    // When
    Optional<User> optUser = userFacade.getUser();

    //Then
    assertNotNull(optUser.isPresent());
    assertEquals(stubEmail, optUser.get().getEmail());
    verify(userRepository).findByEmail(stubEmail);
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void givenTheEmailFromTheAuthentication_whenUserNotFoundInDB_thenCreateNewUser() {
    // Given
    String stubEmail = "nicanor@email.com";
    SecurityContextHolder.getContext().setAuthentication(new StubAuthentication(stubEmail));
    when(userRepository.findByEmail(stubEmail)).thenReturn(empty());
    doAnswer(invocation -> invocation.getArgument(0, User.class)).when(userRepository).save(any(User.class));

    // When
    Optional<User> optUser = userFacade.getUser();

    //Then
    assertNotNull(optUser.isPresent());
    assertEquals(stubEmail, optUser.get().getEmail());
    verify(userRepository).findByEmail(stubEmail);
    verify(userRepository).save(any(User.class));
  }

  @Test
  void givenNoAuthentication_thenReturnsEmptyOptional() {
    // When
    Optional<User> optUser = userFacade.getUser();

    //Then
    assertFalse(optUser.isPresent());
  }

  @Test
  void givenTheAuthentication_whenTheEmailIsEmptyString_thenReturnsEmptyOptions() {
    // Given
    String stubEmail = "";
    SecurityContextHolder.getContext().setAuthentication(new StubAuthentication(stubEmail));

    // When
    Optional<User> optUser = userFacade.getUser();

    //Then
    assertFalse(optUser.isPresent());
  }

  static class StubAuthentication implements Authentication {
    private final String email;

    StubAuthentication(String email) {
      this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return null;
    }

    @Override
    public Object getCredentials() {
      return null;
    }

    @Override
    public Object getDetails() {
      return null;
    }

    @Override
    public Object getPrincipal() {
      return null;
    }

    @Override
    public boolean isAuthenticated() {
      return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
      return email;
    }
  }
}