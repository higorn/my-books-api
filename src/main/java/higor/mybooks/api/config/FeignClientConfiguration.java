package higor.mybooks.api.config;

import com.fasterxml.jackson.databind.Module;
import feign.RequestInterceptor;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

@Configuration
public class FeignClientConfiguration {

//  @Bean
  public Module pageJacksonModule() {
    return new Jackson2HalModule();
//    return new PageJacksonModule();
  }

  @Bean
  public RequestInterceptor requestInterceptor(OAuth2ProtectedResourceDetails details) {
    return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), details);
  }
}
