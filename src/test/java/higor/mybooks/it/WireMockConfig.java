package higor.mybooks.it;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class WireMockConfig {
  private static WireMockServer wireMockServer;

  @Bean(initMethod = "start", destroyMethod = "stop")
  public WireMockServer wireMockServer() {
    if (wireMockServer != null)
      return wireMockServer;
    return wireMockServer = new WireMockServer(9561);
  }
}
