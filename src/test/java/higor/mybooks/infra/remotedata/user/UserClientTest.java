package higor.mybooks.infra.remotedata.user;

import com.github.tomakehurst.wiremock.WireMockServer;
import feign.FeignException;
import feign.RequestInterceptor;
import higor.mybooks.TestConstatns;
import higor.mybooks.domain.user.User;
import higor.mybooks.it.WireMockConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@ContextConfiguration(classes = { WireMockConfig.class })
class UserClientTest {

  @Autowired
  private UserClient     userClient;
  @Autowired
  private WireMockServer wireMockServer;
  @MockBean
  private RequestInterceptor requestInterceptor;
  @MockBean
  private JwtDecoder     jwtDecoder;

  @Test
  void givenAnEmail_whenTheAccountExists_thenReturnsNotEmptyUserOptional() {
    String email = "nicanor@mybooks.com";
    wireMockServer.stubFor(get(urlMatching("/v1/users/search/by-email.*"))
        .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withHeader("Content-Type", "application/hal+json")
            .withBody(TestConstatns.EXPECTED_USER_BY_EMAIL)));

    Optional<User> userOpt = userClient.findByEmail(email);
    assertTrue(userOpt.isPresent());
    assertEquals(email, userOpt.get().getEmail());
  }

  @Test
  void givenAnEmail_whenNotFound_thenReturnsEmptyUserOptional() {
    String email = "nicanor@mybooks.com";
    wireMockServer.stubFor(get(urlMatching("/v1/users/search/by-email.*"))
        .willReturn(aResponse()
            .withStatus(HttpStatus.NOT_FOUND.value())));

    assertThrows(FeignException.NotFound.class, () -> userClient.findByEmail(email));
  }

  @Test
  void whenCreatingUser_thenReturnsHttpStatusCreated_andTheUser() {
    User user = new User().email("nicanor@mybooks.com");
    wireMockServer.stubFor(post("/v1/users/")
        .willReturn(aResponse()
            .withStatus(HttpStatus.CREATED.value())
            .withHeader("Content-Type", "application/hal+json")
            .withBody(TestConstatns.EXPECTED_USER_BY_EMAIL)));

    User savedUser = userClient.create(user);
    assertEquals(1, savedUser.getId());
  }
}