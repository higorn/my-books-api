package higor.mybooks.infra.remotedata.config;

import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.MultipartFormContentProcessor;
import feign.form.spring.SpringFormEncoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.AbstractFormWriter;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

import static feign.form.ContentType.MULTIPART;

@Configuration
public class FeignClientConfig {
  @Autowired
  private ObjectFactory<HttpMessageConverters> messageConverters;

  @Bean
  public RequestInterceptor requestInterceptor(OAuth2ProtectedResourceDetails details) {
    return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), details);
  }

  @Bean
  @ConditionalOnMissingBean
  public Decoder feignDecoder() {
    return new OptionalDecoder(new ResponseEntityDecoder(new BaseEntityDecoder(new PageDecoder(new SpringDecoder(this.messageConverters)))));
  }

  @Bean
  @ConditionalOnClass(name = "higor.mybooks.domain.page.PageRequest")
  @ConditionalOnMissingBean
  public Encoder feignEncoderPageable(ObjectProvider<AbstractFormWriter> formWriterProvider) {
    return new PageRequestEncoder(springEncoder(formWriterProvider));
  }

  private Encoder springEncoder(ObjectProvider<AbstractFormWriter> formWriterProvider) {
    AbstractFormWriter formWriter = formWriterProvider.getIfAvailable();

    if (formWriter != null) {
      return new SpringEncoder(new SpringPojoFormEncoder(formWriter), this.messageConverters);
    }
    else {
      return new SpringEncoder(new SpringFormEncoder(), this.messageConverters);
    }
  }

  private static class SpringPojoFormEncoder extends SpringFormEncoder {

    SpringPojoFormEncoder(AbstractFormWriter formWriter) {
      super();

      MultipartFormContentProcessor processor = (MultipartFormContentProcessor) getContentProcessor(
          MULTIPART);
      processor.addFirstWriter(formWriter);
    }

  }
}
