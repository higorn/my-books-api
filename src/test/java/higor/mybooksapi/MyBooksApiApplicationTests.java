package higor.mybooksapi;

import higor.mybooksapi.application.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = { TestConfig.class })
class MyBooksApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
