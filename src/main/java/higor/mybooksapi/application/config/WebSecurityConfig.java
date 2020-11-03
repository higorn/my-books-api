package higor.mybooksapi.application.config;

import higor.mybooksapi.application.security.JwtGroupsAuthenticationConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
//  String issuerUri;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
          .antMatchers("/api-doc").permitAll()
          .antMatchers("/api-src-doc").permitAll()
          .antMatchers("/api-src-doc/*").permitAll()
          .antMatchers("/swagger-ui/*").permitAll()
          .anyRequest().authenticated()
        .and()
//          .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
//              .jwt(jwt -> jwt.decoder(JwtDecoders.fromIssuerLocation(issuerUri))))
          .oauth2ResourceServer().jwt()
          .jwtAuthenticationConverter(new JwtGroupsAuthenticationConverter());
  }
}
