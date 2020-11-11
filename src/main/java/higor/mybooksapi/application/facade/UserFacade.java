package higor.mybooksapi.application.facade;

import higor.mybooksapi.domain.user.User;
import higor.mybooksapi.domain.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.Optional.*;

@Component
public class UserFacade {
  private final UserRepository userRepository;

  public UserFacade(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Optional<User> getUser() {
    return getEmailFromAuthentication().flatMap(email -> email.isEmpty() ? empty() : getOrCreateUser(email));
  }

  private Optional<String> getEmailFromAuthentication() {
    return ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .map(Authentication::getName);
  }

  private Optional<User> getOrCreateUser(String email) {
    return of(userRepository.findByEmail(email).orElseGet(() -> create(email)));
  }

  private User create(String email) {
    User user = new User();
    user.setEmail(email);
    return userRepository.save(user);
  }

  public static class UserNotFoundException extends RuntimeException {
  }
}
