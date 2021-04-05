package higor.mybooks.infra.remotedata.user;

import higor.mybooks.domain.user.User;
import higor.mybooks.infra.remotedata.DataRestClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(value = "users-service", path = "/v1/users")
public interface UserClient extends DataRestClient<User> {

  @GetMapping(path = "/search/by-email")
  Optional<User> findByEmail(@RequestParam("email") String email);
}
