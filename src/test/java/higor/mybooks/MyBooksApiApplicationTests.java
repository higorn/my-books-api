package higor.mybooks;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
class MyBooksApiApplicationTests {
  @MockBean
  private JwtDecoder jwtDecoder;

	@Test
	void contextLoads() {
	  assertTrue(true);
	}

}
